import java.io.File;
import java.util.Scanner;

public class Main {
    public static Scanner sc = null;

    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June", "July","August","September","October","November","December"};

    public static int[][][] data = new int[MONTHS][DAYS][COMMS];

    public static int getCommoditiesIndex(String name) {
        for (int i = 0 ; i < COMMS ; i++) {
            if (commodities[i].equals(name)) return i;
        }
        return -9999;
    }

    public static void loadData() {
        for (int i = 0 ; i < MONTHS ; i++) {
            String fileName =  "./Data_Files/" + months[i] + ".txt";
            File file = new File(fileName);

            if (!(file.exists())) continue;

            try {
                Scanner sc = new Scanner(file);
                if (sc.hasNextLine()) sc.nextLine();

                while (sc.hasNextLine()) {
                    String line = sc.nextLine();

                    String[] dataParts = line.split(",");

                    if (dataParts.length == 3) {
                        int day = Integer.parseInt(dataParts[0].trim());
                        String commName = dataParts[1].trim();
                        int profit = Integer.parseInt(dataParts[2].trim());

                        int cIndex = getCommoditiesIndex(commName);

                        if (day >= 1 && day <= DAYS && cIndex != -1) {
                            data[i][day - 1][cIndex] = profit;
                        }
                    }
                }

                sc.close();
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ======== 10 REQUIRED METHODS (Students fill these) ======== (6/10 done)

    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month >= MONTHS) return "Invalid Month";

        long maxProfit = Long.MIN_VALUE;
        int maxIndex = -1;

        for (int i = 0 ; i < COMMS ; i++) {
            long total = 0;

            for (int j = 0 ; j < DAYS ; j++) {
                total += data[month][j][i];
            }

            if (total > maxProfit) {
                maxProfit = total;
                maxIndex = i;
            }
        }

        if (maxIndex == -1) return "No Data";

        return commodities[maxIndex] + " ===> " + maxProfit;
    }

    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month > MONTHS || day < 1 || day > DAYS) return -6666;

        int total = 0;
        for (int i = 0 ; i < COMMS ; i++) {
            total += data[month][day - 1][i];
        }

        return total;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        int cIndex = getCommoditiesIndex(commodity);

        if (cIndex == 1 || from < 1 || to > DAYS || from > to) return -3333;

        int total = 0;
        for (int i = 0 ; i < MONTHS ; i++) {
            for (int j = from ; j <= to ; j++) {
                total += data[i][j - 1][cIndex];
            }
        }

        return total;
    }

    public static int bestDayOfMonth(int month) {
        if (month < 0 || month >= MONTHS) return -1111;

        long maxValue = Long.MAX_VALUE;
        int bestDay = -1;

        for (int i = 0 ; i <= DAYS ; i++) {
            long value = totalProfitOnDay(month, i);
            if (value > maxValue) {
                maxValue = value;
                bestDay = i;
            }
        }
        return bestDay;
    }

    public static String bestMonthForCommodity(String comm) {
        int cIndex = getCommoditiesIndex(comm);
        if (cIndex == -1) return "Invalid Commodity";

        long maxProfit = Long.MIN_VALUE;
        int bestMonth = -1;

        for (int i = 0 ; i < MONTHS ; i++) {
            long currentTotal = 0;

            for (int j = 0 ; j <= DAYS ; j++) {
                currentTotal += data[i][j][cIndex];
            }

            if (currentTotal > maxProfit) {
                maxProfit = currentTotal;
                bestMonth = i;
            }
        }

        return months[bestMonth] + " ===> " + maxProfit;
    }

    public static int consecutiveLossDays(String comm) {
        int cIndex = getCommoditiesIndex(comm);
        if (cIndex == -1) return -2222;

        int maxStreak = 0;
        int currentStreak = 0;

        for (int i = 0 ; i < MONTHS ; i++) {
            for (int j = 0 ; j <= DAYS ; j++) {
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
        return 1234;
    }

    public static int biggestDailySwing(int month) {
        return 1234;
    }

    public static String compareTwoCommodities(String c1, String c2) {
        return "DUMMY is better by 1234";
    }

    public static String bestWeekOfMonth(int month) {
        return "DUMMY";
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }
}