package com.company.k0zak.routes

import com.company.k0zak.UserAuthenticator
import com.company.k0zak.dao.EventsDao
import com.company.k0zak.model.Event
import org.http4k.core.*
import org.http4k.format.Jackson.auto
import org.http4k.lens.*
import org.http4k.routing.ResourceLoader
import org.http4k.routing.static

class EventsRoute(private val eventsDao: EventsDao, authentication: UserAuthenticator) {

    data class JsonResponse(val message: String)
    private val responseLens = Body.auto<JsonResponse>().toLens()

    val postNew: HttpHandler = authentication.authenticateFilter.then({ req: Request ->
        val ownerField = FormField.string().required("owner")
        val titleField = FormField.string().required("title")

        val formBody = Body.webForm(Validator.Strict, ownerField, titleField).toLens()

        try {
            val webForm = formBody.extract(req)
            val owner = ownerField.extract(webForm)
            val title = titleField.extract(webForm)
            eventsDao.insertEvent(Event(owner, title))

            responseLens.inject(
                    JsonResponse("A postNew event has been created with the title $title, owned by $owner"), Response(Status.CREATED))

        } catch (e: LensFailure) {
            println(e.message)
            responseLens.inject(
                    JsonResponse("Unable to create event because fields were missing!"), Response(Status.BAD_REQUEST))
        }
    })

    val getNew: HttpHandler = authentication.authenticateFilter.then(static(ResourceLoader.Classpath("public/new")))

}