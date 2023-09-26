package com.telegrammCategory.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**    Сущность дерева категорий     */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "сategoryes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
    /**    Идентификатор узла     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    /**    номер родительского узла     */
    @Column(name = "parent")
    int parent_node_id;
    /**    номер наследника     */
    @Column(name = "seq")
    int seq;
    /**    Имя узла     */
    @Column(name = "name")
    String name;
    /**    Содержание узла     */
    @Column(name = "filling")
    String filling;
    /**    Наличие продолжения    */
    @Column(name = "menu")
    boolean menu;
}
