package no.nav.medlemskap.barnebriller.clients.medloppslag

data class MedlemskapResponse(
    val resultat: MedlemskapResponseResultat,
)

data class MedlemskapResponseResultat(
    val regelId: MedlemskapResponseResultatRegelId,
    val svar: MedlemskapResponseResultatSvar,
    val årsaker: List<MedlemskapResultatÅrsaker>,
)

enum class MedlemskapResponseResultatRegelId {
    REGEL_MEDLEM_KONKLUSJON
}

enum class MedlemskapResponseResultatSvar {
    JA, UAVKLART, NEI
}

data class MedlemskapResultatÅrsaker(
    val regelId: String,
    val avklaring: String,
    val svar: MedlemskapResponseResultatSvar,
    val begrunnelse: String,
)