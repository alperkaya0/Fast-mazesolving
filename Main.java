import java.awt.*;
import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.imageio.ImageIO;

public class Main extends IntiutivelyShortest{

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        //Don't forget to change file and starting,finishing points
        System.out.println("Please enter your username:");
        String username = scanner.nextLine();
        File orIm = new File("C:\\Users\\"+username+"\\Desktop\\maze2.png");
        System.out.println("Please enter starting point and finishing point in this format [startingColumn]x[startingRow] and [finishingColumn]x[finishingRow]");
        String start = scanner.next();
        String finish = scanner.next();
        scanner.close();
        BufferedImage img = null;
        BufferedImage gray = null;
        try {
            img = ImageIO.read(orIm);
            gray = ImageIO.read(orIm);

            for (int i = 0;i<img.getWidth();++i) {
                for (int j = 0;j<img.getHeight();++j) {
                    Color c = new Color(img.getRGB(i,j));
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    int a = c.getAlpha();
                    int gr = (r+g+b) / 3;
                    if (gr>=127) {
                        gr = 255;
                    }
                    if (gr<127) {
                        gr = 0;
                    }
                    Color gC = new Color(gr,gr,gr,a);
                    gray.setRGB(i,j,gC.getRGB());
                }
            }
        }catch (IOException e) {
            System.out.println(e);
        }

        ArrayList<ArrayList<Vertex>> vertices = new ArrayList<>();
        for (int i = 0;i<gray.getWidth();++i) {
            ArrayList<Vertex> temp = new ArrayList<>();
            for (int j = 0;j<gray.getHeight();++j) {
                Color c = new Color(gray.getRGB(i,j));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                int a = c.getAlpha();
                int gr = (r+g+b) / 3;
                if (gr>=127){
                    String name = i + "x" + j;
                    Vertex grVertex = new Vertex(1.0, name);
                    temp.add(grVertex);
                }else{
                    temp.add(null);
                }
            }
            vertices.add(temp);
        }

        for (int i = 0;i<gray.getWidth();++i) {
            for (int j = 0;j<gray.getHeight();++j) {
                Color c = new Color(gray.getRGB(i,j));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                int a = c.getAlpha();
                int gr = (r+g+b) / 3;
                String name = i+"x"+j;
                int[] plusI = {-1,0,1};
                int[] plusJ = {-1,0,1};
                if (gr==255){
                    for (int I : plusI) {
                        for (int J : plusJ) {
                            if (0 <= (i + I) && 0 <= (j + J) && (i + I) < gray.getWidth() && (j + J) < gray.getHeight() && (i+I<vertices.size()) && (j+J<vertices.get(i+I).size())) {
                                Color cALT = new Color(gray.getRGB(i+I,j+J));
                                int rALT = cALT.getRed();
                                int gALT = cALT.getGreen();
                                int bALT = cALT.getBlue();
                                int aALT = cALT.getAlpha();
                                if ((!(J == 0 && I == 0)) && (rALT+gALT+bALT <= 255*3) && (rALT+gALT+bALT>=127*3)) {
                                    String altName = (i + I) + "x" + (j + J);
                                    try{
                                        Edge edge = new Edge(new Vertex[]{vertices.get(i).get(j), vertices.get(i+I).get(j+J)}, 10.0);
                                        vertices.get(i).get(j).addEdge(edge);
                                        vertices.get(i+I).get(j+J).addEdge(edge);
                                        vertices.get(i+I).get(j+J).setName(altName);
                                        vertices.get(i).get(j).setName(name);
                                        System.out.println(vertices.get(i).get(j).getName()+" "+vertices.get(i).get(j).getEdgesTCNames());
                                        System.out.println(vertices.get(i+I).get(j+J).getName()+" "+vertices.get(i+I).get(j+J).getEdgesTCNames());
                                    }catch (Exception e) {
                                        System.out.println(e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("at the end.");
        LinkedList<Vertex> graphVertices = new LinkedList<>();
        for (ArrayList<Vertex> arr:vertices) {
            for (Vertex v:arr) {
                graphVertices.add(v);
            }
        }

        UGraph graph = new UGraph(graphVertices);
        String output = "";
        int start = -1;
        int finish = -1;
        for (int i = 0;i<graphVertices.size();++i) {
            if (graphVertices.get(i) != null){
                if (graphVertices.get(i).getName().equals(start)) {
                    start = i;
                }
            }
        }
        for (int i = 0;i<graphVertices.size();++i) {
            if (graphVertices.get(i) != null){
                if (graphVertices.get(i).getName().equals(finish)) {
                    finish = i;
                }
            }
        }
        if (start != -1 && finish != -1)
            output = IntuitivenessPath(graphVertices.get(start),graphVertices.get(finish),graph);
        else System.out.println("Target is null!");

        LinkedList<int[]> outputArrInt = new LinkedList<>();

        String[] outputArr = output.split(" <= ");
        for (String str:outputArr) {
            System.out.println(str);
            String[] temp = str.split("x");
            if (!str.isEmpty())outputArrInt.add(new int[]{Integer.parseInt(temp[0]),Integer.parseInt(temp[1])});
        }

        for (int[] arr:outputArrInt) {
            Color c = new Color(73,183,210,255);
            gray.setRGB(arr[0],arr[1],c.getRGB());
        }
        boolean successBlue = false;
        try{
            successBlue = ImageIO.write(gray, "PNG", new File("C:\\Users\\"+username+"\\Desktop\\outputBlue.png"));
        }catch (Exception e) {
            System.out.println(e);
        }
        int G = 29;
        int R = 119;
        int B = 209;
        for (int[] arr:outputArrInt) {
            if (R<255){
                R = (int)(Math.random()*255);
            }
            if (G<255){
                G = (int)(Math.random()*255);
            }
            if (B<255){
                B = (int)(Math.random()*255);
            }
            Color c = new Color(R, G, B,255);
            gray.setRGB(arr[0],arr[1],c.getRGB());

        }
        boolean successColorful = false;
        try{
            successColorful = ImageIO.write(gray, "PNG", new File("C:\\Users\\"+username+"\\Desktop\\outputColorful.png"));
        }catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(successColorful?"Colorful one has printed successfully.":"Colorful one couldn't printed!");
        System.out.println(successBlue?"Blue one has printed successfully.\n":"Blue one couldn't printed!\n");
    }
}
