package com.malec.domain.model

abstract class DTO {
    protected val separator = "♂"

    companion object {
        @JvmStatic
        protected val ID = "id"

        @JvmStatic
        protected val DATE = "date"

        @JvmStatic
        protected val DELETED = "deleted"

        @JvmStatic
        protected val COMMENT = "comment"

        @JvmStatic
        protected val STAGES = "stages"

        @JvmStatic
        protected val NAME = "name"
    }
}