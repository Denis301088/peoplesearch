package com.example.peoplesearch.botapi;

/**
 * Возможные состояния бота
 */
public enum BotState {

    INITIAL_MESSAGE,
    ASK_CITY,
    ASK_SURNAME,
    ASK_NAME,
    ASK_PATRONYMIC,
    FILLING_PROFILE,//глобалное значение-возвращает обработчик сообщений заполнение профиля
    PROFILE_FILLED,//профиль заполнен
    RESPONSE_USER,//состояние ответа пользователю
    MENU,
    MENU_INFO,
    MENU_HELP,
//    BUTTON_HANDLING,//глобалное значение-возвращает обработчик кнопок
    SHOW_MAIN_MENU,

}
