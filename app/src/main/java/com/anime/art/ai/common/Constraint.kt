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
            const val PRIVACY_URL = ""
            const val TERMS_URL = ""
            const val DATA_VERSION = 1
        }
    }

    class Iap {
        companion object  {

        }
    }

}