package com.telegrammCategory.model;

import jakarta.persistence.*;
import lombok.*;

/**    Сущность состояния пользователей     */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "userState")
public class UserState {
    /**    ChatId пользователя     */
    @Id
    @Column(name = "id")
    private Integer id;
    /**    уровень меню пользователя     */
    @Column(name = "level")
    private int level;
}
