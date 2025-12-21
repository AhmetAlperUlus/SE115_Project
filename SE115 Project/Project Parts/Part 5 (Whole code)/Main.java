import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static Scanner sc = null;

    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June", "July","August","September","October","November","December"};

    static int[][][] data = new int[MONTHS][DAYS][COMMS];

    public static int getCommodityIndex(String name) {
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(name)) return i;
        }
        return -1;
    }

    public static void loadData() {
        for (int i = 0; i < MONTHS; i++) {
            // We use FileInputStream which takes a String path (Allowed method)
            String fileName = "Data_Files/" + months[i] + ".txt";

            try {
                // Open the file stream
                FileInputStream fis = new FileInputStream(fileName);
                sc = new Scanner(fis);

                if (sc.hasNextLine()) sc.nextLine(); // Skip header

                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] dataParts = line.split(",");

                    if (dataParts.length == 3) {
                        try {
                            int day = Integer.parseInt(dataParts[0].trim());
                            String commName = dataParts[1].trim();
                            int profit = Integer.parseInt(dataParts[2].trim());

                            int cIndex = getCommodityIndex(commName);

                            if (day >= 1 && day <= DAYS && cIndex != -1) {
                                data[i][day - 1][cIndex] = profit;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                sc.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month >= MONTHS) return "Invalid Month";

        long maxProfit = Long.MIN_VALUE;
        int maxIndex = -1;

        for (int i = 0; i < COMMS; i++) {
            long total = 0;
            for (int j = 0; j < DAYS; j++) {
                total += data[month][j][i];
            }

            if (total > maxProfit) {
                maxProfit = total;
                maxIndex = i;
            }
        }

        if (maxIndex == -1) return "No Data";
        return commodities[maxIndex] + " " + maxProfit;
    }

    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month >= MONTHS || day < 1 || day > DAYS) return -6666;

        int total = 0;
        for (int i = 0; i < COMMS; i++) {
            total += data[month][day - 1][i];
        }
        return total;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        int cIndex = getCommodityIndex(commodity);
        if (cIndex == -1 || from < 1 || to > DAYS || from > to) return -3333;

        int total = 0;
        for (int i = 0; i < MONTHS; i++) {
            for (int j = from; j <= to; j++) {
                total += data[i][j - 1][cIndex];
            }
        }
        return total;
    }

    public static int bestDayOfMonth(int month) {
        if (month < 0 || month >= MONTHS) return -1111;

        long maxValue = Long.MIN_VALUE;
        int bestDay = -1;

        for (int i = 1; i <= DAYS; i++) {
            long value = totalProfitOnDay(month, i);
            if (value > maxValue) {
                maxValue = value;
                bestDay = i;
            }
        }
        return bestDay;
    }

    public static String bestMonthForCommodity(String comm) {
        int cIndex = getCommodityIndex(comm);
        if (cIndex == -1) return "Invalid Commodity";

        long maxProfit = Long.MIN_VALUE;
        int bestMonth = -1;

        for (int i = 0; i < MONTHS; i++) {
            long currentTotal = 0;
            for (int j = 0; j < DAYS; j++) {
                currentTotal += data[i][j][cIndex];
            }

            if (currentTotal > maxProfit) {
                maxProfit = currentTotal;
                bestMonth = i;
            }
        }
        return months[bestMonth] + " " + maxProfit;
    }

    public static int consecutiveLossDays(String comm) {
        int cIndex = getCommodityIndex(comm);
        if (cIndex == -1) return -2222;

        int maxStreak = 0;
        int currentStreak = 0;

        for (int i = 0; i < MONTHS; i++) {
            for (int j = 0; j < DAYS; j++) {
                if (data[i][j][cIndex] < 0) currentStreak++;
                else {
                    if (currentStreak > maxStreak) maxStreak = currentStreak;
                    currentStreak = 0;
                }
            }
        }
        if (currentStreak > maxStreak) maxStreak = currentStreak;
        return maxStreak;
    }

    public static int daysAboveThreshold(String comm, int threshold) {
        int cIndex = getCommodityIndex(comm);
        if (cIndex == -1) return -4444;

        int count = 0;
        for (int i = 0; i < MONTHS; i++) {
            for (int j = 0; j < DAYS; j++) {
                if (data[i][j][cIndex] > threshold) count++;
            }
        }
        return count;
    }

    public static int biggestDailySwing(int month) {
        if (month < 0 || month >= MONTHS) return -5555;

        int maxSwing = 0;
        for (int i = 0; i < DAYS - 1; i++) {
            int today = totalProfitOnDay(month, i + 2);
            int yesterday = totalProfitOnDay(month, i + 1);

            int swing = Math.abs(today - yesterday);
            if (swing > maxSwing) maxSwing = swing;
        }
        return maxSwing;
    }

    public static String compareTwoCommodities(String c1, String c2) {
        int cIndex1 = getCommodityIndex(c1);
        int cIndex2 = getCommodityIndex(c2);

        if (cIndex1 == -1 || cIndex2 == -1) return "Invalid Commodity";

        long total1 = 0;
        long total2 = 0;
        for (int i = 0; i < MONTHS; i++) {
            for (int j = 0; j < DAYS; j++) {
                total1 += data[i][j][cIndex1];
                total2 += data[i][j][cIndex2];
            }
        }

        long difference = Math.abs(total1 - total2);
        if (total1 > total2) return c1 + " is better by " + difference;
        else if (total2 > total1) return c2 + " is better by " + difference;
        else return "Both are equal";
    }

    public static String bestWeekOfMonth(int month) {
        if (month < 0 || month >= MONTHS) return "Invalid Month";

        long maxProfit = Long.MIN_VALUE;
        int bestWeek = -1;

        for (int i = 1; i <= 4; i++) {
            long currentWeekProfit = 0;

            int startDay = (i - 1) * 7 + 1;
            int endDay = i * 7;

            for (int j = startDay; j <= endDay; j++) {
                currentWeekProfit += totalProfitOnDay(month, j);
            }

            if (currentWeekProfit > maxProfit) {
                maxProfit = currentWeekProfit;
                bestWeek = i;
            }
        }
        return "Week " + bestWeek + " " + maxProfit;
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded - ready for queries");
    }
}