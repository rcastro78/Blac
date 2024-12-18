package mx.com.comblacsol.networking



class BlacAPI {
    companion object {
        private const val API_URL = "http://3.17.53.133:3030/mobile/connect/v1/"
        fun getBlacAPIService(): IBlacAPI? {
            return RetrofitClient.getClient(API_URL)?.create(IBlacAPI::class.java)
        }
    }
}

/*
class ConnectAPI {
    companion object {
        private const val CNN_API_URL = "http://3.17.53.133:3030/mobile/connect/v1/"
        fun getCnnAPIService(): IBlacAPI? {
            return RetrofitClient.getClient(CNN_API_URL)?.create(IBlacAPI::class.java)
        }
    }
}

class ErpAPI {
    companion object {
        private const val ERP_API_URL = "http://3.17.53.133:6060/erp/blac/v1/"
        fun getErpAPIService(): IBlacAPI? {
            return RetrofitClient.getClient(ERP_API_URL)?.create(IBlacAPI::class.java)
        }
    }
}

 */