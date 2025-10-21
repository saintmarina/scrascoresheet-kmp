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
    pressOnTheStar = "↑ Нажмите на звезду",
    doubleLetterScore = "Двойной счёт буквы",
    doubleWordScore = "Двойной счёт слова",
    tripleLetterScore = "Тройной счёт буквы",
    tripleWordScore = "Тройной счёт слова",
    blankTile = "Пустая фишка",
    tileBackgroundDescription = "Фон фишки",
    logoDescription = "Логотип Scrabble Score Sheet",
    select = "Выбрать",
    pass = "Пропустить",
    endTurn = "Закончить ход",
    addWord = "+ Добавить слово",
    bingo = "Бинго",
    undo = "Отменить",
    endGame = "Закончить игру",
    pressOnALetterInstruction = "↑ Нажмите на букву",
    gridHeaderNames = "Имена\n(Всего)",
    gridHeaderPlayerTurn = "Ход игрока",
    gridLeftoverAccounting = "Подсчёт оставшихся фишек",
    gridMove = "Ход",
    gridSubmitAWordOrPass = "Введите слово или пропустите ход",
    instructionTitleGameScreen = "Инструкции:",
    instructionsTextGameScreen =
        "• Чтобы отправить слово, введите его в поле ввода (белый прямоугольник над кнопками) и нажмите КОНЕЦ ХОДА.\n" +
                "• Если слово использует фишку на премиум-клетке (например, двойное слово), нажмите на эту фишку в окне ввода и выберите соответствующую опцию.\n" +
                "• Если вы ошиблись, используйте ОТМЕНА.\n" +
                "• При подсчёте нескольких слов за один ход нажмите ДОБАВИТЬ СЛОВО.\n" +
                "• Если игрок использует все 7 фишек за один ход, нажмите БИНГО — это добавит бонус в 50 очков.\n" +
                "• Если вы используете ПУСТУЮ фишку (джокер), нажмите на неё и отметьте как пустую.\n" +
                "• Когда все игроки завершат свой последний ход, нажмите КОНЕЦ ИГРЫ. Затем игроки вводят оставшиеся фишки. Игроки с оставшимися фишками теряют эти очки, а игроки без остатка получают очки других.\n" +
                "• Чтобы начать новую игру, нажмите на логотип в верхней части страницы.",
    submitLeftovers = "Введите остатки",
    submitNoLeftovers = "Нет остатков",
    newGame = "Новая игра",
    rateUsOnPlayStore = "Оцените нас в Play Store",
    rateUsOnAppStore = "Оцените нас в App Store",
    submitYourLeftovers = "Введите ваши остатки",
    noLeftovers = "Нет остатков",
    thisIsATieBetween = "Это ничья между:",
    points = "очка",
    wonWith = "выиграл с",
    areYouSureYouWantToStartANewGame = "Вы уверены, что хотите начать новую игру?",
    currentProgressWillBeLost = "Текущий прогресс будет потерян.",
)