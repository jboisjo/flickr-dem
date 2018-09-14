package ca.jboisjoli.jayboisjoli.repository.model.entity

internal data class Photos(val page: Int,
                           val pages: Int,
                           val perpage: Int,
                           val total: String,
                           val photo: MutableList<Photo>)