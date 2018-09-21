package com.jonly.frame.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cond {
    public static final String NOT = "NOT";
    public static final String AND = "AND";
    public static final String OR = "OR";

    private List<Logic> logicList = new ArrayList<Logic>();

    
    public Cond and(Logic logic) {
        Logic and = And.and(logic);
        this.logicList.add(and);
        return this;
    }

    public Cond or(Logic logic) {
        Logic or = Or.or(logic);
        this.logicList.add(or);
        return this;
    }

    public Cond orNot(Logic logic) {
        Logic or = Or.or(Not.not(logic));
        this.logicList.add(or);
        return this;
    }

    public Cond andNot(Logic logic) {
        Logic and = And.and(Not.not(logic));
        this.logicList.add(and);
        return this;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Logic c : logicList) {
            if (c instanceof And) {
                str.append(" ").append(AND).append(" ").append(c);
            } else if (c instanceof Or) {
                str.append(" ").append(OR).append(" ").append(c);
            }
        }
        if (str.length() > 0) {
            return str.substring(4).trim();
        }
        return str.toString();
    }
    
    public Object[] values() {
        List<Object> retlist = new ArrayList<Object>();
        for (Logic logic : logicList) {
            retlist.addAll(Arrays.asList(logic.values()));
        }
        return retlist.toArray();
    }

    public static class Not extends Logic {
        private Logic logic;

        private Not(Logic logic) {
            this.logic = logic;
        }

        public static Logic not(Logic logic) {
            return new Not(logic);
        }

        public String toString() {
            StringBuilder str = new StringBuilder();
            if (logic == null) {
                return str.toString();
            }
            str.append(NOT).append("(").append(logic.toString()).append(")");
            return str.toString();
        }
        @Override
        public Object[] values() {
            return logic.values();
        }
    }

    public static class And extends Logic {
        private List<Logic> list;

        private And(List<Logic> list) {
            this.list = list;
        }

        public static And and(Logic... logics) {
            return new And(Arrays.asList(logics));
        }

        public String toString() {
            StringBuilder str = new StringBuilder();
            for (Logic logic : list) {
                str.append(" ").append(AND).append(" ");
                str.append(logic.toString());
            }
            if (str.length() > 0) {
                return str.substring(AND.length() + 1).trim();
            }
            return str.toString();
        }
        
        @Override
        public Object[] values() {
            List<Object> retlist = new ArrayList<Object>();
            for (Logic logic : list) {
                retlist.addAll(Arrays.asList(logic.values()));
            }
            return retlist.toArray();
        }
    }

    public static class Or extends Logic {
        private List<Logic> list;

        private Or(List<Logic> list) {
            this.list = list;
        }

        public static Or or(Logic... logics) {
            return new Or(Arrays.asList(logics));
        }

        public String toString() {
            StringBuilder str = new StringBuilder();
            for (Logic logic : list) {
                str.append(" ").append(OR).append(" ");
                str.append(logic.toString());
            }
            if (str.length() > 0) {
                return str.substring(OR.length() + 1).trim();
            }
            return str.toString();
        }

        @Override
        public Object[] values() {
            List<Object> retlist = new ArrayList<Object>();
            for (Logic logic : list) {
                retlist.addAll(Arrays.asList(logic.values()));
            }
            return retlist.toArray();
        }
    }

}
