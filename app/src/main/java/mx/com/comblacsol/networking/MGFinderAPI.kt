package mx.com.comblacsol.networking

class MGFinderAPI {
    companion object {
        private const val MG_API_URL = "https://app.blac.com.mx/mgfinder/"
        fun getMGFinderAPIService(): IMGFinderAPI? {
            return RetrofitMGFClient.getClient(MG_API_URL)?.create(IMGFinderAPI::class.java)
        }
    }
}