package org.example.telegramservice.dbservice.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test", schema = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Test {

    @Id
    @Column(name = "chat_Id", unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    int rightAns;
    int wrongAns;
    int prbLeft;
    int currPrb;
    int selectedChapter;

    public Test(int rightAns, int wrongAns, int prbLeft, int currPrb, int selectedChapter) {
        this.rightAns = rightAns;
        this.wrongAns = wrongAns;
        this.prbLeft = prbLeft;
        this.currPrb = currPrb;
        this.selectedChapter = selectedChapter;
    }

}
