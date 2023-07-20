package com.anime.art.ai.common

interface ItemRvClickListener<T> {
    fun onClick(item : T)
    fun scroll(position : Int)
}