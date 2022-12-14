package com.axelor.apps.base.db;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Cacheable
@Table(name = "BASE_PERIOD", indexes = { @Index(columnList = "name"), @Index(columnList = "code"), @Index(columnList = "year") })
public class Period extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BASE_PERIOD_SEQ")
	@SequenceGenerator(name = "BASE_PERIOD_SEQ", sequenceName = "BASE_PERIOD_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Name")
	@NotNull
	private String name;

	@Widget(title = "Code")
	private String code;

	@Widget(title = "Year")
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Year year;

	@Widget(title = "From")
	private LocalDate fromDate;

	@Widget(title = "To")
	private LocalDate toDate;

	@Widget(title = "Status", readonly = true, selection = "base.period.status.select")
	private Integer statusSelect = 1;

	@Widget(title = "Closure date")
	private LocalDateTime closureDateTime;

	@Widget(title = "Adjust History")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "period", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdjustHistory> adjustHistoryList;

	@Widget(title = "Allow expense creation")
	private Boolean allowExpenseCreation = Boolean.TRUE;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Period() {
	}

	public Period(String name, String code) {
		this.name = name;
		this.code = code;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public Integer getStatusSelect() {
		return statusSelect == null ? 0 : statusSelect;
	}

	public void setStatusSelect(Integer statusSelect) {
		this.statusSelect = statusSelect;
	}

	public LocalDateTime getClosureDateTime() {
		return closureDateTime;
	}

	public void setClosureDateTime(LocalDateTime closureDateTime) {
		this.closureDateTime = closureDateTime;
	}

	public List<AdjustHistory> getAdjustHistoryList() {
		return adjustHistoryList;
	}

	public void setAdjustHistoryList(List<AdjustHistory> adjustHistoryList) {
		this.adjustHistoryList = adjustHistoryList;
	}

	/**
	 * Add the given {@link AdjustHistory} item to the {@code adjustHistoryList}.
	 *
	 * <p>
	 * It sets {@code item.period = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addAdjustHistoryListItem(AdjustHistory item) {
		if (getAdjustHistoryList() == null) {
			setAdjustHistoryList(new ArrayList<>());
		}
		getAdjustHistoryList().add(item);
		item.setPeriod(this);
	}

	/**
	 * Remove the given {@link AdjustHistory} item from the {@code adjustHistoryList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeAdjustHistoryListItem(AdjustHistory item) {
		if (getAdjustHistoryList() == null) {
			return;
		}
		getAdjustHistoryList().remove(item);
	}

	/**
	 * Clear the {@code adjustHistoryList} collection.
	 *
	 * <p>
	 * If you have to query {@link AdjustHistory} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearAdjustHistoryList() {
		if (getAdjustHistoryList() != null) {
			getAdjustHistoryList().clear();
		}
	}

	public Boolean getAllowExpenseCreation() {
		return allowExpenseCreation == null ? Boolean.FALSE : allowExpenseCreation;
	}

	public void setAllowExpenseCreation(Boolean allowExpenseCreation) {
		this.allowExpenseCreation = allowExpenseCreation;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof Period)) return false;

		final Period other = (Period) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", getId())
			.add("name", getName())
			.add("code", getCode())
			.add("fromDate", getFromDate())
			.add("toDate", getToDate())
			.add("statusSelect", getStatusSelect())
			.add("closureDateTime", getClosureDateTime())
			.add("allowExpenseCreation", getAllowExpenseCreation())
			.omitNullValues()
			.toString();
	}
}
