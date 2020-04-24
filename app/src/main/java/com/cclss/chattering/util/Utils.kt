package com.cclss.chattering.util

object Utils {
    fun memberSearch(num: String?): String {
        return when (num) {
            "장원영" -> "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-jang-wonyoung.jpg"
            "사쿠라" -> "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-miyawaki-sakura.jpg"
            "조유리" -> "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-jo-yuri.jpg"
            "최예나" -> "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-choi-yena.jpg"
            "안유진" -> "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-an-yujin.jpg"
            "나코" -> "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-yabuki-nako.jpg"
            "권은비" -> "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-kwon-eunbi.jpg"
            "강혜원" -> "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-kang-hyewon.jpg"
            "히토미" -> "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-honda-hitomi.jpg"
            "김채원" -> "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-kim-chaewon.jpg"
            "김민주" -> "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-kim-minju.jpg"
            "이채연" -> "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-lee-chaeyeon.jpg"
            else -> ""
        }
    }
}