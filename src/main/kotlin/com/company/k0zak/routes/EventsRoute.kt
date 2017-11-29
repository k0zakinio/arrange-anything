package com.company.k0zak.routes

import com.company.k0zak.UserAuth
import com.company.k0zak.dao.EventsDao
import com.company.k0zak.dao.UserDao
import com.company.k0zak.model.Event
import org.http4k.core.*
import org.http4k.core.cookie.cookie
import org.http4k.format.Jackson.auto
import org.http4k.lens.*

class EventsRoute(private val eventsDao: EventsDao, private val userDao: UserDao, auth: UserAuth) {

    data class JsonResponse(val message: String)
    private val responseLens = Body.auto<JsonResponse>().toLens()

    val postNew: HttpHandler = auth.authUserFilter.then({ req: Request ->
        val titleField = FormField.string().required("title")
        val dateField = FormField.dateTime().required("event_date")

        val formBody = Body.webForm(Validator.Strict, titleField, dateField).toLens()

        try {
            val webForm = formBody.extract(req)
            val title = titleField.extract(webForm)
            val eventDate = dateField.extract(webForm)
            userDao.getUserFromCookie(req.cookie("aa_session_id")!!.value)
                    ?.let { user -> eventsDao.insertEvent(Event(user.username, title, eventDate)) }

            responseLens.inject(JsonResponse("A postNew event has been created with the title $title"), Response(Status.CREATED))
        } catch (e: LensFailure) {
            e.printStackTrace()
            responseLens.inject(JsonResponse("Unable to create event because fields were missing!"), Response(Status.BAD_REQUEST))
        } catch (e: Exception) {
            e.printStackTrace()
            responseLens.inject(JsonResponse("An unexpected error has occurred"), Response(Status.INTERNAL_SERVER_ERROR))
        }
    })
}