package no.nav.medlemskap.barnebriller.service.pdl

import com.expediagroup.graphql.client.types.GraphQLClientError
import no.nav.medlemskap.barnebriller.rest.objectMapper


open class PdlClientException(message: String, cause: Throwable? = null) : RuntimeException(message, cause) {
    constructor(errors: List<GraphQLClientError>) : this(objectMapper.writeValueAsString(errors))
}

class PdlNotFoundException : PdlClientException("code: $KODE") {
    companion object {
        const val KODE = "not_found"
    }
}

class PdlBadRequestException : PdlClientException("code: $KODE") {
    companion object {
        const val KODE = "bad_request"
    }
}

class PdlUnauthenticatedException : PdlClientException("code: $KODE") {
    companion object {
        const val KODE = "unauthenticated"
    }
}

class PdlHarAdressebeskyttelseException : PdlClientException("Person har adressebeskyttelse")