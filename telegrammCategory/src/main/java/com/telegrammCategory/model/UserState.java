package com.telegrammCategory.model;

import javax.persistence.*;
import lombok.*;

/**    Сущность состояния пользователей     */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
//@Entity
//@Table(name = "userState")
public class UserState {
    /**    ChatId пользователя     */
//    @Id
//    @Column(name = "id")
    private long id;
    /**    уровень меню пользователя     */
//    @Column(name = "level")
    private int level;
    /**    последние действия пользователя     */
//    @Column(name = "lastAction")
    private String lastAction;
}
