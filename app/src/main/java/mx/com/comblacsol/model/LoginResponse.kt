package mx.com.comblacsol.model

data class LoginResponse(
    val token: String,
    val usuario: String
)

data class LoginRequest(
    val usuario:String,
    val password:String

)