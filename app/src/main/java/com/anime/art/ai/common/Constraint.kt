package com.anime.art.ai.common

class Constraint {

    class Sinkin {
        companion object {
            const val DEFAULT_NEGATIVE = "((NSFW)), naked, nude, no clothes, upper body uncovered, breast uncovered, (worst quality, low quality, extra digits:1.4),artist name, nsfw, monochrome, fused face, poorly drawn face, cloned face," +
                    " false body, false face, bad hands, poorly drawn hands, fused eyes, poorly drawn eyes, liquid eyes, false eyes, scary, ugly"
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
            const val DATA_VERSION = 3
            const val CREATE_ART_WORK_COST = 5
            const val MAKE_VARIATIONS_COST = 3
        }
    }
    class AIGeneration{
        companion object{
            const val URL = "https://api.getimg.ai/v1/stable-diffusion/"
            const val KEY = "key-1x1r032KSaWH5RtCLC3Iwgtj1QLfS8RrXSpHv6NbssvSQa1MBQrZpAOnrUKvCO2QkWeQnI4wpHN0h7YC2E7HMyjzUlAhUkwE"
        }
    }
    companion object{
        const val BASE_URL : String = "http://13.57.181.100/api/"
        const val CREATE_ARTWORK : String ="CREATE_ARTWORK"
        const val PURCHASED_WEEK : String ="PURCHASED_WEEK"
        const val PURCHASED_YEAR : String ="PURCHASED_YEAR"
        const val PURCHASED_CREDITS : String ="PURCHASED_CREDITS"
        const val MAKE_VARIATIONS : String = "MAKE_VARIATIONS"
        const val DAILY_REWARD : String = "DAILY_REWARD"
    }

}