package com.steelsoftware.scrascoresheet.logic

import com.steelsoftware.scrascoresheet.i18n.Locales

val scoreListsMap = mapOf(
    Locales.ENGLISH to mapOf(
        'a' to 1, 'e' to 1, 'i' to 1, 'o' to 1, 'u' to 1,
        'l' to 1, 'n' to 1, 'r' to 1, 's' to 1, 't' to 1,
        'd' to 2, 'g' to 2, 'b' to 3, 'c' to 3, 'm' to 3, 'p' to 3,
        'f' to 4, 'h' to 4, 'v' to 4, 'w' to 4, 'y' to 4,
        'k' to 5, 'j' to 8, 'x' to 8, 'q' to 10, 'z' to 10
    ),
    Locales.RUSSIAN to mapOf(
        'о' to 1, 'а' to 1, 'е' to 1, 'и' to 1, 'н' to 1,
        'р' to 1, 'с' to 1, 'т' to 1, 'в' to 1, 'д' to 2,
        'к' to 2, 'л' to 2, 'п' to 2, 'у' to 2, 'м' to 2,
        'б' to 3, 'г' to 3, 'ь' to 3, 'я' to 3, 'ё' to 3,
        'ы' to 4, 'й' to 4, 'з' to 5, 'ж' to 5, 'х' to 5,
        'ц' to 5, 'ч' to 5, 'ш' to 8, 'э' to 8, 'ю' to 8,
        'ф' to 10, 'щ' to 10, 'ъ' to 10
    ),
    Locales.SPANISH to mapOf(
        'a' to 1, 'e' to 1, 'o' to 1, 'i' to 1, 's' to 1,
        'n' to 1, 'l' to 1, 'r' to 1, 'u' to 1, 't' to 1,
        'd' to 2, 'g' to 2, 'c' to 3, 'b' to 3, 'm' to 3, 'p' to 3,
        'h' to 4, 'f' to 4, 'v' to 4, 'y' to 4,
        'q' to 5, 'j' to 8, 'ñ' to 8, 'x' to 8, 'z' to 10
    )
)

fun isLetterAllowed(letter: Char, language: String): Boolean =
    scoreListsMap[language]?.containsKey(letter.lowercaseChar()) == true