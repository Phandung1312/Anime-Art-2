package com.anime.art.ai.common

class Constraint {

    class Sinkin {
        companion object {
            const val DEFAULT_NEGATIVE = "((NSFW)), naked, nude, no clothes, upper body uncovered, breast uncovered, (worst quality, low quality, extra digits:1.4),artist name, nsfw, monochrome, fused face, poorly drawn face, cloned face, false body, false face, bad hands, malformation, poorly drawn hands, fused eyes, poorly drawn eyes, liquid eyes, false eyes, scary, ugly"
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
            const val URL = "https://stablediffusionapi.com/api/v1/enterprise/"
            const val KEY1 = "2sqt7csmnbkd5p"
            const val KEY2 = "iv43mluk5t0bez"
            const val KEY3 = "b6j7boydonsjmy"
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