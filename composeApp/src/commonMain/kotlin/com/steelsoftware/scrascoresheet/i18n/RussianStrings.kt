package com.steelsoftware.scrascoresheet.i18n

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = Locales.RUSSIAN, default = false)
val RussianStrings = Strings(
    hello = "Привет",
    player = "Игрок ",
    appDescriptionForWelcomeScreen = "Scra Score Sheet помогает вести счёт и заменяет традиционные бумажные таблицы для настольной игры в слова Scrabble. Наслаждайтесь игрой с друзьями и семьёй, пока этот счётчик делает всю математику за вас.",
    appInstructionsForWelcomeScreen = "Просто введите имена игроков в порядке, в котором они будут ходить, и нажмите кнопку НАЧАТЬ.",
    appSelectScoringLanguageWelcomeScreen = "Выберите язык подсчёта очков:",
    startButton = "НАЧАТЬ",
    featuresTitleWelcomeScreen = "Возможности",
    featuresListWelcomeScreen =
        "• Поддерживает официальные правила подсчёта очков Scrabble, включая учёт оставшихся фишек\n" +
                "• Показывает детальный прогресс игры\n" +
                "• Поддерживает неограниченное количество отмен (undo)\n" +
                "• Восстанавливает таблицу очков при повторном запуске приложения",

    limitationTitleWelcomeScreen = "Ограничения",
    limitationsListWelcomeScreen =
        "• Не проверяет слова по словарю Scrabble\n" +
                "• Поддерживает только английский, испанский и русский языки\n" +
                "• Не сохраняет завершённые таблицы очков в архив",
    gameInProgressTitleWelcomeScreen = "У вас есть незавершённая игра.",
    wouldYouLikeToResumeTitleWelcomeScreen = "Хотите продолжить её?",
    noButton = "Нет",
    yesButton = "Да",
)