package com.mycompany.hrms.data.entity.travel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "proofId")
@Entity
public class ExpensesProofs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long proofId;

    @Column(nullable = false)
    private String proofFilePath;

    private String publicId;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "expenseId")
    private Expenses expenses;

    public long getProofId() {
        return proofId;
    }

    public void setProofId(long proofId) {
        this.proofId = proofId;
    }

    public String getProofFilePath() {
        return proofFilePath;
    }

    public void setProofFilePath(String proofFilePath) {
        this.proofFilePath = proofFilePath;
    }

    public Expenses getExpenses() {
        return expenses;
    }

    public void setExpenses(Expenses expenses) {
        this.expenses = expenses;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}
