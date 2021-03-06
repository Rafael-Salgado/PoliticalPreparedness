package com.example.android.politicalpreparedness.utils

fun getStateNameFromCode(code: String): String {
    return when (code) {
        "aa" -> "armed forces america"
        "ae"-> "armed forces"
        "al" -> "alabama"
        "ap" -> "armed forces pacific"
        "ak" -> "alaska"
        "ar" -> "arkansas"
        "as" -> "american samoa"
        "ca" -> "california"
        "co" -> "colorado"
        "ct" -> "connecticut"
        "de" -> "delaware"
        "dc" -> "districtofcolumbia"
        "fl" -> "florida"
        "ga" -> "georgia"
        "hi" -> "hawaii"
        "id" -> "idaho"
        "il" -> "illinois"
        "in" -> "indiana"
        "ia" -> "iowa"
        "ks" -> "kansas"
        "ky" -> "kentucky"
        "la" -> "louisiana"
        "me" -> "maine"
        "md" -> "maryland"
        "ma" -> "massachusetts"
        "mi" -> "michigan"
        "mn" -> "minnesota"
        "ms" -> "mississippi"
        "mo" -> "missouri"
        "mt" -> "montana"
        "ne" -> "nebraska"
        "nv" -> "nevada"
        "nh" -> "newhampshire"
        "nj" -> "newjersey"
        "nm" -> "newmexico"
        "ny" -> "newyork"
        "nc" -> "northcarolina"
        "nd" -> "northdakota"
        "oh" -> "ohio"
        "ok" -> "oklahoma"
        "or" -> "oregon"
        "pa" -> "pennsylvania"
        "ri" -> "rhodeisland"
        "sc" -> "southcarolina"
        "sd" -> "southdakota"
        "tn" -> "tennessee"
        "tx" -> "texas"
        "ut" -> "utah"
        "vt" -> "vermont"
        "va" -> "virginia"
        "wa" -> "washington"
        "wv" -> "westvirginia"
        "wi" -> "wisconsin"
        "wy" -> "wyoming"
        else -> ""
    }
}