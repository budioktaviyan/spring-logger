package id.kotlin.logger.model

import java.time.LocalDateTime

data class Log(val ipAddress: String,
               val userIdentifier: String,
               val date: LocalDateTime?,
               val method: String,
               val response: String,
               val sequence: String)