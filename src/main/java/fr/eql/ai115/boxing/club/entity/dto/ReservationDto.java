package fr.eql.ai115.boxing.club.entity.dto;

import java.time.LocalDate;

public class ReservationDto {
    private Long id;
    private LocalDate orderedDate;
    private LocalDate validateDate;
    private boolean isValidate;

    public Long getId() {
        return id;
    }
    public LocalDate getOrderedDate() {
        return orderedDate;
    }
    public LocalDate getValidateDate() {
        return validateDate;
    }
    public boolean isValidate() {
        return isValidate;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setOrderedDate(LocalDate orderedDate) {
        this.orderedDate = orderedDate;
    }
    public void setValidateDate(LocalDate validateDate) {
        this.validateDate = validateDate;
    }
    public void setValidate(boolean validate) {
        isValidate = validate;
    }
}
