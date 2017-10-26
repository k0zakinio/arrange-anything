package com.company.k0zak.routes

import com.company.k0zak.dao.EventsDao
import com.company.k0zak.model.Event
import org.http4k.core.*
import org.http4k.format.Jackson.auto
import org.http4k.lens.*

class EventsRoute(private val eventsDao: EventsDao) {

    data class JsonResponse(val message: String)
    private val responseLens = Body.auto<JsonResponse>().toLens()

    val new: HttpHandler = { req: Request ->
        val ownerField = FormField.string().required("owner")
        val titleField = FormField.string().required("title")

        val formBody = Body.webForm(Validator.Strict, ownerField, titleField).toLens()

        try {
            val webform = formBody.extract(req)
            val owner = ownerField.extract(webform)
            val title = titleField.extract(webform)
            eventsDao.insertEvent(Event(owner, title))

            responseLens.inject(
                    JsonResponse("A new event has been created with the title $title, owned by $owner"), Response(Status.CREATED))

        } catch (e: LensFailure) {
            println(e.message)
            responseLens.inject(
                    JsonResponse("Unable to create event because fields were missing!}"), Response(Status.BAD_REQUEST))
        }
    }

}