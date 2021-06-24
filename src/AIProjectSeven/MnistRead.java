package AIProjectSeven;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MnistRead {

    public static final String TRAIN_IMAGES_FILE = "data/train-images.idx3-ubyte";
    public static final String TRAIN_LABELS_FILE = "data/train-labels.idx1-ubyte";
    public static final String TEST_IMAGES_FILE = "data/t10k-images.idx3-ubyte";
    public static final String TEST_LABELS_FILE = "data/t10k-labels.idx1-ubyte";

    /**
     * change bytes into a hex string. 
     * @param bytes bytes
     * @return the returned hex string
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        System.out.println("sb.toString() = "+sb.toString());
        return sb.toString();
    }

    /**
     * get images of 'train' or 'test'
     *
     * @param fileName the file of 'train' or 'test' about image
     * @return one row show a `picture`
     */
    public static double[][] getImages(String fileName) {
    	double[][] x = null;
        try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName))) {
            byte[] bytes = new byte[4];
            bin.read(bytes, 0, 4);
            if (!"00000803".equals(bytesToHex(bytes))) {                        // 读取魔数
                throw new RuntimeException("Please select the correct file!");
            } else {
                bin.read(bytes, 0, 4);
                int number = Integer.parseInt(bytesToHex(bytes), 16);           // 读取样本总数
                bin.read(bytes, 0, 4);
                int xPixel = Integer.parseInt(bytesToHex(bytes), 16);           // 读取每行所含像素点数
                bin.read(bytes, 0, 4);
                int yPixel = Integer.parseInt(bytesToHex(bytes), 16);           // 读取每列所含像素点数
                x = new double[number][xPixel * yPixel];
                for (int i = 0; i < number; i++) {
                	double[] element = new double[xPixel * yPixel];
                    for (int j = 0; j < xPixel * yPixel; j++) {
//                        element[j] = bin.read();                                // 逐一读取像素值
                        // normalization
                        element[j] = bin.read() / 255.0;
                    }
                    x[i] = element;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return x;
    }

    /**
     * get labels of `train` or `test`
     *
     * @param fileName the file of 'train' or 'test' about label
     * @return
     */
    public static double[] getLabels(String fileName) {
    	double[] y = null;
        try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName))) {
            byte[] bytes = new byte[4];
            bin.read(bytes, 0, 4);
            if (!"00000801".equals(bytesToHex(bytes))) {
                throw new RuntimeException("Please select the correct file!");
            } else {
                bin.read(bytes, 0, 4);
                int number = Integer.parseInt(bytesToHex(bytes), 16);
                y = new double[number];
                for (int i = 0; i < number; i++) {
                    y[i] = bin.read();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return y;
    }
    
    /**
     * draw a gray picture and the image format is JPEG.
     *
     * @param pixelValues pixelValues and ordered by column.
     * @param width       width
     * @param high        high
     * @param fileName    image saved file.
     */
    public static void drawGrayPicture(double[] pixelValues, int width, int high, String fileName) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, high, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < high; j++) {
                int pixel = (int) (255 - pixelValues[i * high + j]);
                int value = pixel + (pixel << 8) + (pixel << 16);   // r = g = b 时，正好为灰度
                bufferedImage.setRGB(j, i, value);
            }
        }
        ImageIO.write(bufferedImage, "JPEG", new File(fileName));
    }

    public static void main(String[] args) throws IOException {
        double[][] images1 = getImages(TRAIN_IMAGES_FILE);
        double[] labels1 = getLabels(TRAIN_LABELS_FILE);
        
        for(int i = 0;i<=5;i++) {
        	System.out.println("i = "+ i + ",labels = "+labels1[i]+" image:");
        	for(int j = 0;j<28;j++) {
        		for(int k =0;k<28;k++) {
        			System.out.print(String.format("%3.1f", images1[i][j*28+k]));
        		}System.out.println();
        	}
        	
        }
        
        double[][] images2 = getImages(TEST_IMAGES_FILE);
        double[] labels2 = getLabels(TEST_LABELS_FILE);

        drawGrayPicture(images1[7],28,28,"data/pic.jpeg");
        
        System.out.println();
    }
}
