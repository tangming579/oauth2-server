package com.tm.auth.mbg.model;

import java.util.ArrayList;
import java.util.List;

public class OauthAuthorityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OauthAuthorityExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andTargetIdIsNull() {
            addCriterion("target_id is null");
            return (Criteria) this;
        }

        public Criteria andTargetIdIsNotNull() {
            addCriterion("target_id is not null");
            return (Criteria) this;
        }

        public Criteria andTargetIdEqualTo(String value) {
            addCriterion("target_id =", value, "targetId");
            return (Criteria) this;
        }

        public Criteria andTargetIdNotEqualTo(String value) {
            addCriterion("target_id <>", value, "targetId");
            return (Criteria) this;
        }

        public Criteria andTargetIdGreaterThan(String value) {
            addCriterion("target_id >", value, "targetId");
            return (Criteria) this;
        }

        public Criteria andTargetIdGreaterThanOrEqualTo(String value) {
            addCriterion("target_id >=", value, "targetId");
            return (Criteria) this;
        }

        public Criteria andTargetIdLessThan(String value) {
            addCriterion("target_id <", value, "targetId");
            return (Criteria) this;
        }

        public Criteria andTargetIdLessThanOrEqualTo(String value) {
            addCriterion("target_id <=", value, "targetId");
            return (Criteria) this;
        }

        public Criteria andTargetIdLike(String value) {
            addCriterion("target_id like", value, "targetId");
            return (Criteria) this;
        }

        public Criteria andTargetIdNotLike(String value) {
            addCriterion("target_id not like", value, "targetId");
            return (Criteria) this;
        }

        public Criteria andTargetIdIn(List<String> values) {
            addCriterion("target_id in", values, "targetId");
            return (Criteria) this;
        }

        public Criteria andTargetIdNotIn(List<String> values) {
            addCriterion("target_id not in", values, "targetId");
            return (Criteria) this;
        }

        public Criteria andTargetIdBetween(String value1, String value2) {
            addCriterion("target_id between", value1, value2, "targetId");
            return (Criteria) this;
        }

        public Criteria andTargetIdNotBetween(String value1, String value2) {
            addCriterion("target_id not between", value1, value2, "targetId");
            return (Criteria) this;
        }

        public Criteria andMethodsIsNull() {
            addCriterion("methods is null");
            return (Criteria) this;
        }

        public Criteria andMethodsIsNotNull() {
            addCriterion("methods is not null");
            return (Criteria) this;
        }

        public Criteria andMethodsEqualTo(String value) {
            addCriterion("methods =", value, "methods");
            return (Criteria) this;
        }

        public Criteria andMethodsNotEqualTo(String value) {
            addCriterion("methods <>", value, "methods");
            return (Criteria) this;
        }

        public Criteria andMethodsGreaterThan(String value) {
            addCriterion("methods >", value, "methods");
            return (Criteria) this;
        }

        public Criteria andMethodsGreaterThanOrEqualTo(String value) {
            addCriterion("methods >=", value, "methods");
            return (Criteria) this;
        }

        public Criteria andMethodsLessThan(String value) {
            addCriterion("methods <", value, "methods");
            return (Criteria) this;
        }

        public Criteria andMethodsLessThanOrEqualTo(String value) {
            addCriterion("methods <=", value, "methods");
            return (Criteria) this;
        }

        public Criteria andMethodsLike(String value) {
            addCriterion("methods like", value, "methods");
            return (Criteria) this;
        }

        public Criteria andMethodsNotLike(String value) {
            addCriterion("methods not like", value, "methods");
            return (Criteria) this;
        }

        public Criteria andMethodsIn(List<String> values) {
            addCriterion("methods in", values, "methods");
            return (Criteria) this;
        }

        public Criteria andMethodsNotIn(List<String> values) {
            addCriterion("methods not in", values, "methods");
            return (Criteria) this;
        }

        public Criteria andMethodsBetween(String value1, String value2) {
            addCriterion("methods between", value1, value2, "methods");
            return (Criteria) this;
        }

        public Criteria andMethodsNotBetween(String value1, String value2) {
            addCriterion("methods not between", value1, value2, "methods");
            return (Criteria) this;
        }

        public Criteria andPathsIsNull() {
            addCriterion("paths is null");
            return (Criteria) this;
        }

        public Criteria andPathsIsNotNull() {
            addCriterion("paths is not null");
            return (Criteria) this;
        }

        public Criteria andPathsEqualTo(String value) {
            addCriterion("paths =", value, "paths");
            return (Criteria) this;
        }

        public Criteria andPathsNotEqualTo(String value) {
            addCriterion("paths <>", value, "paths");
            return (Criteria) this;
        }

        public Criteria andPathsGreaterThan(String value) {
            addCriterion("paths >", value, "paths");
            return (Criteria) this;
        }

        public Criteria andPathsGreaterThanOrEqualTo(String value) {
            addCriterion("paths >=", value, "paths");
            return (Criteria) this;
        }

        public Criteria andPathsLessThan(String value) {
            addCriterion("paths <", value, "paths");
            return (Criteria) this;
        }

        public Criteria andPathsLessThanOrEqualTo(String value) {
            addCriterion("paths <=", value, "paths");
            return (Criteria) this;
        }

        public Criteria andPathsLike(String value) {
            addCriterion("paths like", value, "paths");
            return (Criteria) this;
        }

        public Criteria andPathsNotLike(String value) {
            addCriterion("paths not like", value, "paths");
            return (Criteria) this;
        }

        public Criteria andPathsIn(List<String> values) {
            addCriterion("paths in", values, "paths");
            return (Criteria) this;
        }

        public Criteria andPathsNotIn(List<String> values) {
            addCriterion("paths not in", values, "paths");
            return (Criteria) this;
        }

        public Criteria andPathsBetween(String value1, String value2) {
            addCriterion("paths between", value1, value2, "paths");
            return (Criteria) this;
        }

        public Criteria andPathsNotBetween(String value1, String value2) {
            addCriterion("paths not between", value1, value2, "paths");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}