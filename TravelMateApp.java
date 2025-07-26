import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;  

public class TravelMateApp {
    public static void main(String[] args) {
        System.out.println("Welcome to Enhanced TravelMate - Smart Travel Planning System!");
        System.out.println("=".repeat(65));
        new TravelMate().run();
    }
}

class TravelMate {
    private Map<String, Destination> destinations = new HashMap<>();
    private Map<String, Map<String, Integer>> graph = new HashMap<>();
    private BST destinationBST = new BST();
    private BinaryTree categoryTree = new BinaryTree();
    private Stack<String> visitHistory = new Stack<>();
    private Queue<String> recommendationQueue = new LinkedList<>();
    private List<TravelPlan> travelPlans = new ArrayList<>();
    private Set<String> visitedDestinations = new HashSet<>();
    private TreeSet<Destination> ratingTree = new TreeSet<>(
        Comparator.comparingDouble(d -> -d.rating)
    );
    
    private Map<String, Integer> categoryCount = new HashMap<>();
    private int totalVisits = 0;
    
    public void run() {
        Scanner sc = new Scanner(System.in);
        try {
            loadSampleData();
            
            while (true) {
                displayMenu();
                int choice = getIntInput(sc, "Choice: ");

                try {
                    switch (choice) {
                        case 0 -> loadSampleData();
                        case 1 -> addDestination(sc);
                        case 2 -> addConnection(sc);
                        case 3 -> showRecommendations();
                        case 4 -> findShortestPath(sc);
                        case 5 -> visitDestination(sc);
                        case 6 -> showMST();
                        case 7 -> createTravelPlan(sc);
                        case 8 -> showTravelPlans();
                        case 9 -> showAnalytics();
                        case 10 -> showVisitHistory();
                        case 11 -> searchDestinations(sc);
                        case 12 -> {
                            System.out.println("Thanks for using Enhanced TravelMate!");
                            System.out.println("Safe travels!");
                            return;
                        }
                        default -> System.out.println("Invalid choice. Please try again.");
                    }
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
                
                System.out.println("\nPress Enter to continue...");
                sc.nextLine();
            }
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
    
    private void displayMenu() {
        System.out.println("\n" + "=".repeat(65));
        System.out.println("ENHANCED TRAVELMATE - SMART TRAVEL PLANNING SYSTEM");
        System.out.println("=".repeat(65));
        System.out.println("DATA MANAGEMENT:");
        System.out.println("DATA & FILE:");
        System.out.println("  0. Load Data from Files");
        System.out.println("  1. Add Destination");
        System.out.println("  2. Add Connection");
         System.out.println();
        System.out.println("RECOMMENDATION & PATH:");
        System.out.println("  3. Show Recommendations");
        System.out.println("  4. Find Shortest Path");
        System.out.println("  5. Visit Destination");
         System.out.println();
        System.out.println("TRAVEL PLAN:");
        System.out.println("  6. Show MST");
        System.out.println("  7. Create Travel Plan");
        System.out.println("  8. Show Travel Plans");
         System.out.println();
        System.out.println("ANALYTICS & SEARCH:");
        System.out.println("  9. Show Analytics");
        System.out.println(" 10. Show Visit History");
        System.out.println(" 11. Search Destinations");
        System.out.println(" 12. Exit");
        System.out.println("=".repeat(65));
    }
    
    private void loadSampleData() {
        try {
            addSampleDestination("D001", "Keraton Yogyakarta", "Historical", 4.8, 3);
            addSampleDestination("D002", "Candi Borobudur", "Historical", 4.9, 4);
            addSampleDestination("D003", "Candi Prambanan", "Historical", 4.7, 4);
            addSampleDestination("D004", "Malioboro Street", "Shopping", 4.5, 2);
            addSampleDestination("D005", "Taman Sari", "Historical", 4.3, 2);
            addSampleDestination("D006", "Gunung Merapi", "Nature", 4.6, 3);
            addSampleDestination("D007", "Pantai Parangtritis", "Beach", 4.4, 2);
            addSampleDestination("D008", "Museum Sonobudoyo", "Museum", 4.2, 2);
            addSampleDestination("D009", "Alun Alun Kidul", "Cultural", 4.3, 1);
            addSampleDestination("D010", "Museum Ullen Sentalu", "Museum", 4.6, 3);
            addSampleDestination("D011", "Tebing Breksi", "Nature", 4.4, 2);
            addSampleDestination("D012", "Bukit Bintang", "Nature", 4.5, 2);
            addSampleDestination("D013", "Hutan Pinus Mangunan", "Nature", 4.3, 2);
            addSampleDestination("D014", "Goa Jomblang", "Nature", 4.5, 4);
            addSampleDestination("D015", "Kalibiru National Park", "Nature", 4.4, 3);
            addSampleDestination("D016", "Candi Ijo", "Historical", 4.2, 2);
            addSampleDestination("D017", "Taman Pintar", "Educational", 4.1, 2);
            addSampleDestination("D018", "De Mata Trick Eye Museum", "Entertainment", 4.0, 1);
            addSampleDestination("D019", "Pantai Indrayanti", "Beach", 4.5, 3);
            addSampleDestination("D020", "Sindu Kusuma Edupark", "Entertainment", 4.2, 1);
            addSampleDestination("D021", "Candi Ratu Boko", "Historical", 4.6, 3);
            addSampleDestination("D022", "Pantai Drini", "Beach", 4.3, 2);
            addSampleDestination("D023", "Pantai Siung", "Beach", 4.2, 3);
            addSampleDestination("D024", "Pantai Baron", "Beach", 4.4, 2);
            addSampleDestination("D025", "Pantai Kukup", "Beach", 4.1, 2);
            addSampleDestination("D026", "Air Terjun Sri Gethuk", "Nature", 4.3, 3);
            addSampleDestination("D027", "Taman Budaya Yogyakarta", "Cultural", 4.2, 2);
            addSampleDestination("D028", "Museum Affandi", "Museum", 4.4, 2);
            addSampleDestination("D029", "Puncak Becici", "Nature", 4.3, 2);
            addSampleDestination("D030", "Kids Fun Park", "Entertainment", 4.0, 1);
            addSampleDestination("D031", "Kebun Binatang Gembira Loka", "Family", 4.2, 2);
            addSampleDestination("D032", "Desa Wisata Kasongan", "Cultural", 4.1, 2);
            addSampleDestination("D033", "Desa Wisata Tembi", "Cultural", 4.1, 2);
            addSampleDestination("D034", "Museum Batik Yogyakarta", "Museum", 4.0, 2);
            addSampleDestination("D035", "Watu Giring", "Nature", 4.2, 2);
            addSampleDestination("D036", "Bukit Klangon", "Nature", 4.3, 3);
            addSampleDestination("D037", "Embung Nglanggeran", "Nature", 4.4, 3);
            addSampleDestination("D038", "Gunung Kidul Gunung Api Purba", "Nature", 4.6, 4);
            addSampleDestination("D039", "Pantai Sadranan", "Beach", 4.2, 2);
            addSampleDestination("D040", "Pantai Nglambor", "Beach", 4.3, 3);
            addSampleDestination("D041", "Pantai Wediombo", "Beach", 4.4, 3);
            addSampleDestination("D042", "Pantai Sundak", "Beach", 4.3, 2);
            addSampleDestination("D043", "Stonehenge Merapi", "Nature", 4.1, 2);
            addSampleDestination("D044", "Tlogo Putri Kaliurang", "Nature", 4.0, 2);
            addSampleDestination("D045", "Taman Wisata Kaliurang", "Nature", 4.1, 2);
            addSampleDestination("D046", "Candi Sambisari", "Historical", 4.3, 2);
            addSampleDestination("D047", "Candi Kalasan", "Historical", 4.2, 2);
            addSampleDestination("D048", "Candi Plaosan", "Historical", 4.4, 3);
            addSampleDestination("D049", "Agrowisata Bhumi Merah Putih", "Agro", 4.1, 2);
            addSampleDestination("D050", "HeHa Sky View", "Entertainment", 4.5, 3);

            addSampleConnection("D019", "D014", 30);
            addSampleConnection("D019", "D023", 20);
            addSampleConnection("D019", "D022", 15);
            addSampleConnection("D019", "D041", 35);
            addSampleConnection("D019", "D040", 30);
            addSampleConnection("D019", "D007", 50);
            addSampleConnection("D019", "D039", 25);
            addSampleConnection("D018", "D020", 5);
            addSampleConnection("D018", "D017", 3);
            addSampleConnection("D018", "D004", 5);
            addSampleConnection("D017", "D001", 5);
            addSampleConnection("D017", "D020", 10);
            addSampleConnection("D017", "D031", 10);
            addSampleConnection("D017", "D009", 5);
            addSampleConnection("D017", "D004", 3);
            addSampleConnection("D016", "D003", 7);
            addSampleConnection("D016", "D046", 12);
            addSampleConnection("D016", "D011", 5);
            addSampleConnection("D016", "D048", 10);
            addSampleConnection("D015", "D036", 25);
            addSampleConnection("D015", "D002", 45);
            addSampleConnection("D015", "D037", 20);
            addSampleConnection("D014", "D013", 45);
            addSampleConnection("D014", "D041", 60);
            addSampleConnection("D014", "D038", 40);
            addSampleConnection("D013", "D024", 50);
            addSampleConnection("D013", "D023", 58);
            addSampleConnection("D013", "D022", 55);
            addSampleConnection("D013", "D007", 35);
            addSampleConnection("D012", "D036", 22);
            addSampleConnection("D012", "D050", 18);
            addSampleConnection("D012", "D029", 20);
            addSampleConnection("D011", "D003", 10);
            addSampleConnection("D011", "D010", 10);
            addSampleConnection("D011", "D021", 7);
            addSampleConnection("D011", "D048", 12);
            addSampleConnection("D010", "D003", 15);
            addSampleConnection("D010", "D043", 15);
            addSampleConnection("D010", "D006", 20);
            addSampleConnection("D050", "D029", 15);
            addSampleConnection("D050", "D049", 10);
            addSampleConnection("D028", "D034", 3);
            addSampleConnection("D028", "D027", 3);
            addSampleConnection("D027", "D001", 5);
            addSampleConnection("D027", "D034", 4);
            addSampleConnection("D027", "D031", 7);
            addSampleConnection("D027", "D004", 3);
            addSampleConnection("D026", "D037", 25);
            addSampleConnection("D025", "D024", 5);
            addSampleConnection("D025", "D022", 10);
            addSampleConnection("D025", "D042", 10);
            addSampleConnection("D025", "D007", 58);
            addSampleConnection("D024", "D022", 10);
            addSampleConnection("D024", "D007", 60);
            addSampleConnection("D024", "D039", 15);
            addSampleConnection("D023", "D041", 20);
            addSampleConnection("D022", "D040", 15);
            addSampleConnection("D021", "D003", 12);
            addSampleConnection("D021", "D048", 12);
            addSampleConnection("D020", "D030", 10);
            addSampleConnection("D020", "D004", 10);
            addSampleConnection("D039", "D040", 10);
            addSampleConnection("D038", "D002", 55);
            addSampleConnection("D038", "D035", 20);
            addSampleConnection("D038", "D041", 25);
            addSampleConnection("D038", "D006", 50);
            addSampleConnection("D038", "D037", 18);
            addSampleConnection("D036", "D044", 10);
            addSampleConnection("D036", "D043", 8);
            addSampleConnection("D036", "D006", 30);
            addSampleConnection("D033", "D032", 5);
            addSampleConnection("D033", "D005", 7);
            addSampleConnection("D032", "D001", 10);
            addSampleConnection("D032", "D009", 10);
            addSampleConnection("D031", "D004", 7);
            addSampleConnection("D009", "D001", 2);
            addSampleConnection("D009", "D008", 3);
            addSampleConnection("D009", "D005", 5);
            addSampleConnection("D008", "D001", 3);
            addSampleConnection("D008", "D004", 2);
            addSampleConnection("D007", "D042", 62);
            addSampleConnection("D006", "D002", 40);
            addSampleConnection("D006", "D045", 28);
            addSampleConnection("D006", "D044", 25);
            addSampleConnection("D006", "D043", 25);
            addSampleConnection("D005", "D001", 2);
            addSampleConnection("D005", "D004", 3);
            addSampleConnection("D004", "D001", 1);
            addSampleConnection("D048", "D003", 10);
            addSampleConnection("D048", "D047", 8);
            addSampleConnection("D048", "D002", 30);
            addSampleConnection("D003", "D047", 20);
            addSampleConnection("D003", "D002", 25);
            addSampleConnection("D003", "D046", 15);
            addSampleConnection("D047", "D046", 10);
            addSampleConnection("D045", "D044", 5);
            addSampleConnection("D045", "D043", 7);
            addSampleConnection("D042", "D041", 8);
            addSampleConnection("D041", "D040", 12);

            System.out.println("Sample data loaded successfully!");
            System.out.println(destinations.size() + " destinations and " + countTotalConnections() + " connections loaded.");

        } catch (Exception e) {
            System.err.println("Error loading sample data: " + e.getMessage());
        }
    }
    
    private void addSampleDestination(String id, String name, String category, double rating, int price) {
        Destination d = new Destination(id, name, category, rating, price);
        destinations.put(id, d);
        graph.put(id, new HashMap<>());
        destinationBST.insert(d);
        ratingTree.add(d);
        categoryTree.insert(category);
        
        categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
    }
    
    private void addSampleConnection(String from, String to, int distance) {
        if (graph.containsKey(from) && graph.containsKey(to)) {
            graph.get(from).put(to, distance);
            graph.get(to).put(from, distance);
        }
    }

    void addDestination(Scanner sc) {
        try {
            System.out.println("\nADD NEW DESTINATION");
            System.out.println("-".repeat(30));
            
            String id = getStringInput(sc, "Enter ID (e.g., D009): ");
            if (destinations.containsKey(id)) {
                System.out.println("Destination ID already exists!");
                return;
            }
            
            String name = getStringInput(sc, "Enter Name: ");
            String category = getStringInput(sc, "Enter Category (Beach/Historical/Nature/Shopping/City): ");
            double rating = getDoubleInput(sc, "Enter Rating (1.0-5.0): ");
            int price = getIntInput(sc, "Enter Price Level (1-5): ");
            
            if (rating < 1.0 || rating > 5.0) {
                System.out.println("Rating must be between 1.0 and 5.0!");
                return;
            }
            
            if (price < 1 || price > 5) {
                System.out.println("Price level must be between 1 and 5!");
                return;
            }
            
            Destination d = new Destination(id, name, category, rating, price);
            destinations.put(id, d);
            graph.put(id, new HashMap<>());
            destinationBST.insert(d);
            ratingTree.add(d);
            categoryTree.insert(category);
            
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            
            System.out.println("Destination added successfully!");
            System.out.println(d);
            
        } catch (Exception e) {
            System.err.println("Error adding destination: " + e.getMessage());
        }
    }
    
    void addConnection(Scanner sc) {
        try {
            System.out.println("\nADD CONNECTION BETWEEN DESTINATIONS");
            System.out.println("-".repeat(40));
            
            if (destinations.size() < 2) {
                System.out.println("Need at least 2 destinations to create a connection!");
                return;
            }
            
            System.out.println("Available destinations:");
            for (String id : destinations.keySet()) {
                System.out.println("  " + id + " - " + destinations.get(id).name);
            }
            
            String from = getStringInput(sc, "Enter From ID: ");
            String to = getStringInput(sc, "Enter To ID: ");
            int dist = getIntInput(sc, "Enter Distance (km): ");
            
            if (!destinations.containsKey(from) || !destinations.containsKey(to)) {
                System.out.println("Invalid destination IDs!");
                return;
            }
            
            if (from.equals(to)) {
                System.out.println("Cannot connect destination to itself!");
                return;
            }
            
            if (dist <= 0) {
                System.out.println("Distance must be positive!");
                return;
            }
            
            graph.get(from).put(to, dist);
            graph.get(to).put(from, dist);
            
            System.out.println("Connection added successfully!");
            System.out.println(destinations.get(from).name + " <-> " + 
                             destinations.get(to).name + " (" + dist + " km)");
            
        } catch (Exception e) {
            System.err.println("Error adding connection: " + e.getMessage());
        }
    }
    
    void showRecommendations() {
    try {
        System.out.println("\nTOP DESTINATION RECOMMENDATIONS (YOGYAKARTA)");
        System.out.println("=".repeat(70));
        
        if (destinations.isEmpty()) {
            System.out.println("No destinations available!");
            return;
        }
        
        System.out.printf("%-5s %-30s %-15s %-10s %-10s%n", 
                         "Rank", "Destination", "Category", "Rating", "Price");
        System.out.println("-".repeat(70));
        
        int rank = 1;
        for (Destination d : destinationBST.inOrderTraversal()
                                          .stream()
                                          .sorted((a,b) -> Double.compare(b.rating, a.rating))
                                          .collect(Collectors.toList())) {
            
            System.out.printf("%-5d %-30s %-15s %-10.1f %-10s%n",
                rank++,
                d.name,
                d.category,
                d.rating,
                getPriceIndicator(d.price));  

            
            if (rank <= 6) {
                recommendationQueue.offer(d.id);
            }
        }
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("CATEGORY BREAKDOWN");
        System.out.println("-".repeat(70));
        
        categoryCount.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> 
                System.out.printf("  %-15s %2d destinations%n",
                    entry.getKey() + ":",
                    entry.getValue()));
        
        System.out.println("\nPrice Scale: $ (Budget) to $$$$$ (Luxury)");
        
    } catch (Exception e) {
        System.err.println("Error showing recommendations: " + e.getMessage());
    }
}

private String getPriceIndicator(int price) {
    return "$".repeat(Math.max(1, price)) + 
           " ".repeat(Math.max(0, 5 - price));
}
    
    void findShortestPath(Scanner sc) {
        try {
            System.out.println("\nSHORTEST PATH FINDER");
            System.out.println("-".repeat(35));
            
            if (destinations.isEmpty()) {
                System.out.println("No destinations available!");
                return;
            }
            
            System.out.println("Available destinations:");
            for (String id : destinations.keySet()) {
                System.out.println("  " + id + " - " + destinations.get(id).name);
            }
            
            String start = getStringInput(sc, "Enter Start ID: ");
            String end = getStringInput(sc, "Enter End ID: ");
            
            if (!destinations.containsKey(start) || !destinations.containsKey(end)) {
                System.out.println("Invalid destination IDs!");
                return;
            }
            
            if (start.equals(end)) {
                System.out.println("Start and end destinations cannot be the same!");
                return;
            }
            
            DijkstraResult result = dijkstra(start, end);
            
            if (result.distances.get(end) == Integer.MAX_VALUE) {
                System.out.println("No path found between these destinations!");
                return;
            }
            
            System.out.println("\nShortest Path Found!");
            System.out.println("From: " + destinations.get(start).name);
            System.out.println("To: " + destinations.get(end).name);
            System.out.println("Distance: " + result.distances.get(end) + " km");
            
            List<String> path = reconstructPath(result.previous, start, end);
            System.out.println("Path: ");
            for (int i = 0; i < path.size(); i++) {
                System.out.print("  " + destinations.get(path.get(i)).name);
                if (i < path.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
            
            System.out.println("\nAll distances from " + destinations.get(start).name + ":");
            for (String id : result.distances.keySet()) {
                if (!id.equals(start)) {
                    int dist = result.distances.get(id);
                    System.out.println("  " + destinations.get(id).name + ": " + 
                                     (dist == Integer.MAX_VALUE ? "∞" : dist + " km"));
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error finding shortest path: " + e.getMessage());
        }
    }
    
    DijkstraResult dijkstra(String start, String target) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
            Map.Entry.comparingByValue()
        );
        Set<String> visited = new HashSet<>();
        
        for (String id : destinations.keySet()) {
            distances.put(id, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.add(new AbstractMap.SimpleEntry<>(start, 0));
        
        while (!pq.isEmpty()) {
            String current = pq.poll().getKey();
            
            if (!visited.add(current)) continue;
            
            if (current.equals(target)) break;
            
            Map<String, Integer> neighbors = graph.get(current);
            if (neighbors != null) {
                for (Map.Entry<String, Integer> edge : neighbors.entrySet()) {
                    String neighbor = edge.getKey();
                    int newDistance = distances.get(current) + edge.getValue();
                    
                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        previous.put(neighbor, current);
                        pq.add(new AbstractMap.SimpleEntry<>(neighbor, newDistance));
                    }
                }
            }
        }
        
        return new DijkstraResult(distances, previous);
    }
    
    private List<String> reconstructPath(Map<String, String> previous, String start, String end) {
        List<String> path = new ArrayList<>();
        String current = end;
        
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }
        
        Collections.reverse(path);
        return path;
    }
    
    void visitDestination(Scanner sc) {
        try {
            System.out.println("\nVISIT DESTINATION");
            System.out.println("-".repeat(25));
            
            if (destinations.isEmpty()) {
                System.out.println("No destinations available!");
                return;
            }
            
            System.out.println("Available destinations:");
            for (String id : destinations.keySet()) {
                System.out.println("  " + id + " - " + destinations.get(id).name);
            }
            
            String id = getStringInput(sc, "Enter Destination ID to visit: ");
            
            if (!destinations.containsKey(id)) {
                System.out.println("Destination not found!");
                return;
            }
            
            Destination dest = destinations.get(id);
            
            visitHistory.push(id);
            visitedDestinations.add(id);
            totalVisits++;
            
            System.out.println("Visited successfully!");
            System.out.println(dest);
            System.out.println("Visit time: " + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            ));
            
            Map<String, Integer> nearby = graph.get(id);
            if (nearby != null && !nearby.isEmpty()) {
                System.out.println("\nNearby destinations:");
                List<Map.Entry<String, Integer>> sortedNearby = new ArrayList<>(nearby.entrySet());
                sortedNearby.sort(Map.Entry.comparingByValue());
                
                for (Map.Entry<String, Integer> entry : sortedNearby) {
                    System.out.println("  " + destinations.get(entry.getKey()).name + 
                                     " - " + entry.getValue() + " km");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error visiting destination: " + e.getMessage());
        }
    }
    
    void showMST() {
        try {
            System.out.println("\nMINIMUM SPANNING TREE - OPTIMAL ROUTE NETWORK");
            System.out.println("-".repeat(55));
            
            if (destinations.isEmpty()) {
                System.out.println("No destinations available!");
                return;
            }
            
            Set<String> visited = new HashSet<>();
            PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
            List<Edge> mstEdges = new ArrayList<>();
            int totalWeight = 0;
            
            String start = destinations.keySet().iterator().next();
            visited.add(start);
            
            Map<String, Integer> startEdges = graph.get(start);
            if (startEdges != null) {
                for (Map.Entry<String, Integer> entry : startEdges.entrySet()) {
                    pq.add(new Edge(start, entry.getKey(), entry.getValue()));
                }
            }
            
            System.out.println("Building optimal route network...");
            System.out.println("Starting from: " + destinations.get(start).name);
            System.out.println();
            
            while (!pq.isEmpty() && visited.size() < destinations.size()) {
                Edge edge = pq.poll();
                
                if (visited.contains(edge.to)) continue;
                
                visited.add(edge.to);
                mstEdges.add(edge);
                totalWeight += edge.weight;
                
                System.out.println(destinations.get(edge.from).name + " <-> " + 
                                 destinations.get(edge.to).name + " (" + edge.weight + " km)");
                
                Map<String, Integer> newEdges = graph.get(edge.to);
                if (newEdges != null) {
                    for (Map.Entry<String, Integer> entry : newEdges.entrySet()) {
                        if (!visited.contains(entry.getKey())) {
                            pq.add(new Edge(edge.to, entry.getKey(), entry.getValue()));
                        }
                    }
                }
            }
            
            System.out.println("\nMST SUMMARY:");
            System.out.println("Total edges: " + mstEdges.size());
            System.out.println("Total distance: " + totalWeight + " km");
            System.out.println("Connected destinations: " + visited.size() + "/" + destinations.size());
            
            if (visited.size() < destinations.size()) {
                System.out.println("Some destinations are not connected to the network!");
            }
            
        } catch (Exception e) {
            System.err.println("Error creating MST: " + e.getMessage());
        }
    }
    
    void createTravelPlan(Scanner sc) {
        try {
            System.out.println("\nCREATE TRAVEL PLAN");
            System.out.println("-".repeat(30));
            
            if (destinations.isEmpty()) {
                System.out.println("No destinations available!");
                return;
            }
            
            String planName = getStringInput(sc, "Enter plan name: ");
            int days = getIntInput(sc, "Enter number of days: ");
            
            if (days <= 0) {
                System.out.println("Number of days must be positive!");
                return;
            }
            
            TravelPlan plan = new TravelPlan(planName, days);
            
            System.out.println("Available destinations:");
            for (String id : destinations.keySet()) {
                System.out.println("  " + id + " - " + destinations.get(id).name);
            }
            
            for (int day = 1; day <= days; day++) {
                System.out.println("\nDay " + day + ":");
                String destId = getStringInput(sc, "Enter destination ID (or 'skip' to skip): ");
                
                if (!destId.equalsIgnoreCase("skip")) {
                    if (destinations.containsKey(destId)) {
                        plan.addDestination(day, destId);
                        System.out.println("Added " + destinations.get(destId).name + " to Day " + day);
                    } else {
                        System.out.println("Invalid destination ID!");
                        day--; 
                    }
                }
            }
            
            travelPlans.add(plan);
            System.out.println("\nTravel plan created successfully!");
            System.out.println(plan.getPlanSummary(destinations));
            
        } catch (Exception e) {
            System.err.println("Error creating travel plan: " + e.getMessage());
        }
    }
    
    void showTravelPlans() {
        try {
            System.out.println("\nYOUR TRAVEL PLANS");
            System.out.println("-".repeat(25));
            
            if (travelPlans.isEmpty()) {
                System.out.println("No travel plans created yet!");
                return;
            }
            
            for (int i = 0; i < travelPlans.size(); i++) {
                TravelPlan plan = travelPlans.get(i);
                System.out.println((i + 1) + ". " + plan.getPlanSummary(destinations));
                System.out.println();
            }
            
        } catch (Exception e) {
            System.err.println("Error showing travel plans: " + e.getMessage());
        }
    }
    
    void showAnalytics() {
        try {
            System.out.println("\nTRAVEL ANALYTICS");
            System.out.println("-".repeat(25));
            
            if (destinations.isEmpty()) {
                System.out.println("No destinations available!");
                return;
            }
            
            System.out.println("Destination Statistics:");
            System.out.println("  Total destinations: " + destinations.size());
            System.out.println("  Total visits: " + totalVisits);
            System.out.println("  Unique visited: " + visitedDestinations.size());
            
            System.out.println("\nTop Rated Destinations:");
            int count = 1;
            for (Destination d : ratingTree) {
                if (count > 5) break;
                System.out.printf("  %d. %s (%.1f/5.0)%n", count++, d.name, d.rating);
            }
            
            System.out.println("\nNetwork Statistics:");
            System.out.println("  Total connections: " + countTotalConnections());
            System.out.println("  Average connections per destination: " + 
                             String.format("%.2f", getAverageConnections()));
            
            System.out.println("\nCategories:");
            for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
                System.out.printf("  %-15s: %2d destinations%n", 
                               entry.getKey(), entry.getValue());
            }
            
        } catch (Exception e) {
            System.err.println("Error showing analytics: " + e.getMessage());
        }
    }
    
    void showVisitHistory() {
        try {
            System.out.println("\nVISIT HISTORY");
            System.out.println("-".repeat(25));
            
            if (visitHistory.isEmpty()) {
                System.out.println("No visit history yet!");
                return;
            }
            
            System.out.println("Recent visits (newest first):");
            Stack<String> tempStack = new Stack<>();
            tempStack.addAll(visitHistory);
            
            int count = 1;
            while (!tempStack.isEmpty()) {
                String id = tempStack.pop();
                System.out.printf("%2d. %s%n", count++, destinations.get(id).name);
            }
            
        } catch (Exception e) {
            System.err.println("Error showing visit history: " + e.getMessage());
        }
    }
    
    void searchDestinations(Scanner sc) {
        try {
            System.out.println("\nSEARCH DESTINATIONS");
            System.out.println("-".repeat(25));
            
            if (destinations.isEmpty()) {
                System.out.println("No destinations available!");
                return;
            }
            
            String query = getStringInput(sc, "Enter search term (name/category): ").toLowerCase();
            
            List<Destination> results = destinations.values().stream()
                .filter(d -> d.name.toLowerCase().contains(query) || 
                            d.category.toLowerCase().contains(query))
                .sorted((a, b) -> Double.compare(b.rating, a.rating))
                .collect(Collectors.toList());
            
            if (results.isEmpty()) {
                System.out.println("No matching destinations found!");
                return;
            }
            
            System.out.println("Search Results (" + results.size() + "):");
            for (int i = 0; i < results.size(); i++) {
                Destination d = results.get(i);
                System.out.printf("%2d. %-25s %-15s Rating: %.1f  Price: %d/5%n",
                               i+1, d.name, "(" + d.category + ")", d.rating, d.price);
            }
            
        } catch (Exception e) {
            System.err.println("Error searching destinations: " + e.getMessage());
        }
    }
    
    private int getIntInput(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
    
    private double getDoubleInput(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
    
    private String getStringInput(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }
    
    private int countTotalConnections() {
        return graph.values().stream()
            .mapToInt(Map::size)
            .sum() / 2;
    }
    
    private double getAverageConnections() {
        if (destinations.isEmpty()) return 0;
        return (double)countTotalConnections() * 2 / destinations.size();
    }
    
    static class Destination {
        String id, name, category;
        double rating;
        int price;
        
        Destination(String id, String name, String category, double rating, int price) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.rating = rating;
            this.price = price;
        }
        
        @Override
        public String toString() {
            return String.format("%s (%s) - Rating: %.1f, Price: %d/5", 
                               name, category, rating, price);
        }
    }
    
    static class TravelPlan {
        String name;
        int days;
        Map<Integer, List<String>> dailyPlans;
        
        TravelPlan(String name, int days) {
            this.name = name;
            this.days = days;
            this.dailyPlans = new HashMap<>();
            for (int i = 1; i <= days; i++) {
                dailyPlans.put(i, new ArrayList<>());
            }
        }
        
        void addDestination(int day, String destId) {
            if (day >= 1 && day <= days) {
                dailyPlans.get(day).add(destId);
            }
        }
        
        String getPlanSummary(Map<String, Destination> destinations) {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(" (").append(days).append(" days):\n");
            
            for (int day = 1; day <= days; day++) {
                sb.append("  Day ").append(day).append(": ");
                if (dailyPlans.get(day).isEmpty()) {
                    sb.append("Free day");
                } else {
                    sb.append(dailyPlans.get(day).stream()
                        .map(id -> destinations.get(id).name)
                        .collect(Collectors.joining(" -> ")));
                }
                sb.append("\n");
            }
            
            return sb.toString();
        }
    }
    
    static class BST {
        Node root;
        
        static class Node {
            Destination data;
            Node left, right;
            
            Node(Destination data) {
                this.data = data;
            }
        }
        
        void insert(Destination data) {
            root = insert(root, data);
        }
        
        private Node insert(Node node, Destination data) {
            if (node == null) return new Node(data);
            
            int cmp = data.id.compareTo(node.data.id);
            if (cmp < 0) node.left = insert(node.left, data);
            else if (cmp > 0) node.right = insert(node.right, data);
            
            return node;
        }
        
        List<Destination> inOrderTraversal() {
            List<Destination> result = new ArrayList<>();
            inOrder(root, result);
            return result;
        }
        
        private void inOrder(Node node, List<Destination> result) {
            if (node != null) {
                inOrder(node.left, result);
                result.add(node.data);
                inOrder(node.right, result);
            }
        }
    }
    
    static class BinaryTree {
        Node root;
        
        static class Node {
            String data;
            Node left, right;
            
            Node(String data) {
                this.data = data;
            }
        }
        
        void insert(String data) {
            root = insert(root, data);
        }
        
        private Node insert(Node node, String data) {
            if (node == null) return new Node(data);
            
            int cmp = data.compareTo(node.data);
            if (cmp < 0) node.left = insert(node.left, data);
            else if (cmp > 0) node.right = insert(node.right, data);
            
            return node;
        }
    }
    
    static class DijkstraResult {
        Map<String, Integer> distances;
        Map<String, String> previous;
        
        DijkstraResult(Map<String, Integer> distances, Map<String, String> previous) {
            this.distances = distances;
            this.previous = previous;
        }
    }
    
    static class Edge {
        String from, to;
        int weight;
        
        Edge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
}
