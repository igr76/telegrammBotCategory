package com.telegrammCategory.model;

//import javax.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


/**    Сущность состояния пользователей     */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "user_state")
public class UserState {
    /**    ChatId пользователя     */
    @Id
//    @Column(name = "id")
    private long id;
    /**    уровень меню пользователя     */
//    @Column(name = "level")
    private int level;
    /**    последние действия пользователя     */
//    @Column(name = "lastAction")
    private String lastAction;
}
