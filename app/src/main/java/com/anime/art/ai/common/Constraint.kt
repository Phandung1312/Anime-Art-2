package com.anime.art.ai.common

class Constraint {

    class Sinkin {
        companion object {
            const val DEFAULT_NEGATIVE = "(character out of frame)1.4, (worst quality)1.2, (low quality)1.6, (normal quality)1.6, lowres, (monochrome)1.1, (grayscale)1.3, acnes, skin blemishes, bad anatomy, DeepNegative,(fat)1.1, bad hands, text, error, missing fingers, extra limbs, missing limbs, extra digits, fewer digits, cropped, jpeg artifacts, signature, watermark, furry, elf ears"
            const val DEFAULT_MODEL = "anything_4_0"

            const val URL = "https://sinkin.ai/api/inference"
            const val KEY = ""
        }
    }

    class Info {
        companion object {
            const val MAIL_SUPPORT = "hoangdo.bravo@gmail.com"
            const val PRIVACY_URL = "http://www.stackoverflow.com"
            const val TERMS_URL = "https://www.youtube.com/"
            const val DATA_VERSION = 2
            const val DEFAULT_QUALITY = 512
        }
    }

    class Iap {
        companion object  {

        }
    }
    class AIGeneration{
        companion object{
            const val URL = "https://api.getimg.ai/v1/stable-diffusion/"
            const val KEY = "key-jF3RWX1QTULE09qZo97jcFnleOCpANj92PLnjLAPzZONoGrgrNxjwLz3laotG6XfoUDSRJYlK6FEyjzSNFDccOzBYFmHOYx"
        }
    }
    companion object{
        const val BASE_URL : String = "http://13.57.181.100/api/"
        const val CREATE_ARTWORK : String ="CREATE_ARTWORK"
        const val PURCHASED_WEEK : String ="PURCHASED_WEEK"
        const val PURCHASED_YEAR : String ="PURCHASED_YEAR"
    }

}