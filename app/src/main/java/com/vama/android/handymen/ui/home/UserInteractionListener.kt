package com.vama.android.handymen.ui.home

import com.vama.android.handymen.model.UserModelView

/**
 * Interface définissant toutes les interactions possibles avec un utilisateur dans la liste
 */
interface UserInteractionListener {
    /**
     * Appelé lorsque l'utilisateur clique sur l'icône favori
     */
    fun onFavoriteClick(user: UserModelView)

    /**
     * Appelé lorsque l'utilisateur clique sur l'icône supprimer
     */
    fun onDeleteClick(user: UserModelView)

    /**
     * Appelé lorsque l'utilisateur clique sur l'icône partager
     */
    fun onShareClick(user: UserModelView)

    /**
     * Appelé lorsque l'utilisateur clique sur l'élément entier
     * Utile pour la navigation vers le détail
     */
    fun onUserClick(user: UserModelView)
}