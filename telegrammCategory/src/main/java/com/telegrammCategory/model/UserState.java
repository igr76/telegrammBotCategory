package com.telegrammCategory.model;

import jakarta.persistence.*;
import lombok.*;

/**    Сущность дерева категорий     */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "userState")
public class UserState {
    /**    Идентификатор узла     */
    @Id
    @Column(name = "id")
    private Integer id;
    /**    номер наследника     */
    @Column(name = "level")
    private int level;
}
