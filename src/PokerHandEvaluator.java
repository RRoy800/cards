import java.util.*; //This Hand Evalutor was written by the Google AI Overview

public class PokerHandEvaluator {

    public static class HandResult implements Comparable<HandResult> {
        int rank;
        List<Integer> tiebreak;   

        public HandResult(int rank, List<Integer> tiebreak) {
            this.rank = rank;
            this.tiebreak = tiebreak;
        }

        @Override
        public int compareTo(HandResult other) {
            if (this.rank != other.rank)
                return this.rank - other.rank;

            for (int i = 0; i < Math.min(this.tiebreak.size(), other.tiebreak.size()); i++) {
                if (!this.tiebreak.get(i).equals(other.tiebreak.get(i))) {
                    return this.tiebreak.get(i) - other.tiebreak.get(i);
                }
            }
            return 0;
        }
    }

    public static HandResult evaluate(List<Card> cards) {

        List<Integer> values = new ArrayList<>();
        Map<Integer, Integer> counts = new HashMap<>();
        Map<String, Integer> suits = new HashMap<>();

        for (Card c : cards) {
            int val = getRankValue(c.value);
            values.add(val);
            counts.put(val, counts.getOrDefault(val, 0) + 1);
            suits.put(c.suit, suits.getOrDefault(c.suit, 0) + 1);
        }

        Collections.sort(values);
        Collections.reverse(values);

        boolean flush = suits.values().stream().anyMatch(v -> v >= 5);
        boolean straight = isStraight(new HashSet<>(values));

        List<Integer> four = new ArrayList<>();
        List<Integer> three = new ArrayList<>();
        List<Integer> pairs = new ArrayList<>();
        List<Integer> singles = new ArrayList<>();

        for (int val : counts.keySet()) {
            int count = counts.get(val);
            if (count == 4) four.add(val);
            else if (count == 3) three.add(val);
            else if (count == 2) pairs.add(val);
            else singles.add(val);
        }

        Collections.sort(four, Collections.reverseOrder());
        Collections.sort(three, Collections.reverseOrder());
        Collections.sort(pairs, Collections.reverseOrder());
        Collections.sort(singles, Collections.reverseOrder());

        
        if (flush && straight) {
            if (values.contains(14) && values.contains(13) &&
                values.contains(12) && values.contains(11) &&
                values.contains(10)) {
                return new HandResult(10, List.of(14)); // Royal Flush
            }
            return new HandResult(9, List.of(Collections.max(values)));
        }

        if (!four.isEmpty())
            return new HandResult(8, combine(four, singles));

        if (!three.isEmpty() && !pairs.isEmpty())
            return new HandResult(7, combine(three, pairs));

        if (flush)
            return new HandResult(6, values);

        if (straight)
            return new HandResult(5, List.of(Collections.max(values)));

        if (!three.isEmpty())
            return new HandResult(4, combine(three, singles));

        if (pairs.size() >= 2)
            return new HandResult(3, combine(pairs, singles));

        if (pairs.size() == 1)
            return new HandResult(2, combine(pairs, singles));

        return new HandResult(1, values);
    }

    private static boolean isStraight(Set<Integer> values) {
        List<Integer> list = new ArrayList<>(values);
        Collections.sort(list);

        for (int i = 0; i <= list.size() - 5; i++) {
            if (list.get(i+4) - list.get(i) == 4)
                return true;
        }

        if (values.contains(14) &&
            values.contains(2) &&
            values.contains(3) &&
            values.contains(4) &&
            values.contains(5))
            return true;

        return false;
    }

    private static List<Integer> combine(List<Integer> primary, List<Integer> secondary) {
        List<Integer> result = new ArrayList<>();
        result.addAll(primary);
        result.addAll(secondary);
        return result;
    }

    private static int getRankValue(String rank) {
        switch (rank) {
            case "2": return 2;
            case "3": return 3;
            case "4": return 4;
            case "5": return 5;
            case "6": return 6;
            case "7": return 7;
            case "8": return 8;
            case "9": return 9;
            case "10": return 10;
            case "J": return 11;
            case "Q": return 12;
            case "K": return 13;
            case "A": return 14;
        }
        return 0;
    }
}
