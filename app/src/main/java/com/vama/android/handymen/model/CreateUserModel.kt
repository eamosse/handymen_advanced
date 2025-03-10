package com.vama.android.handymen.model

/**
 * Modèle dédié à la création d'un utilisateur
 * Respecte le principe de responsabilité unique (SRP)
 * en séparant les données nécessaires à la création
 * de l'entité User complète utilisée pour la persistance
 */
data class CreateUserModel(
    val name: String,
    val address: String,
    val phoneNumber: String,
    val aboutMe: String,
    val webSite: String
) {
    fun isValid(): Boolean {
        // Validation de base - peut être étendue selon les besoins
        return name.isNotBlank() && phoneNumber.isNotBlank()
    }

    /**
     * Message d'erreur de validation si les données sont invalides
     */
    fun getValidationErrorMessage(): String? {
        return when {
            name.isBlank() -> "Le nom est requis"
            phoneNumber.isBlank() -> "Le numéro de téléphone est requis"
            else -> null
        }
    }
}