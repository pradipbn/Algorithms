import static java.lang.Math.*;
import java.awt.*;

public class SeamCarver {
	private Picture currentPicture;
	private double[][] pixelEnergy;
	private Color[][] pixelColor;
	private Picture verticalSeamCarve;

	public SeamCarver(Picture picture)  {
	   // create a seam carver object based on the given picture
		int picWidth = picture.width();
		int picHeight = picture.height();
		currentPicture = picture;
		pixelColor = new Color[picWidth][picHeight];

		for (int i = 0; i < picHeight; i++) {
			for (int j = 0; j < picWidth; j++) {
				pixelColor[j][i] = picture.get(j, i); 	
			}
		}
	}

	private void transposeColorMatrix() {
		Color[][] transposedColorMatrix = new Color[height()][width()];	
		for (int i = 0; i < height(); i++) {
			for (int j = 0; j < width(); j++) {
				transposedColorMatrix[i][j] = pixelColor[j][i];
			}
		}
		pixelColor = transposedColorMatrix;
	}

	private void CreatePictureFromColorMatrix(int width, int height) {
		verticalSeamCarve = new Picture(width, height);
		for (int i = 0; i < width; i++) {
			for	(int j = 0; j < height; j++) {
				verticalSeamCarve.set(i, j, pixelColor[i][j]);
			}
		}

		currentPicture = verticalSeamCarve;
	}

	public Picture picture() {
		// current picture
		return currentPicture;
	}
	public int width() {
		// width of current picture
		return currentPicture.width();
	}
	public int height() {
		// height of current picture
		return currentPicture.height();
	}
	public double energy(int x, int y) {
		// energy of pixel at column x and row y
		if (x == 0 || y == 0 ||
			(x == width() - 1) || y == (height() - 1)) {
			return 195075;
		} else {
			double rxp = pixelColor[x - 1][y].getRed();
			double gxp = pixelColor[x - 1][y].getGreen();
			double bxp = pixelColor[x - 1][y].getBlue();
			double rxn = pixelColor[x + 1][y].getRed();
			double gxn = pixelColor[x + 1][y].getGreen();
			double bxn = pixelColor[x + 1][y].getBlue();

			//double rx  = pow((abs(rxp - rxn)), 2);
			double rx  = pow(abs(rxp - rxn), 2.0);
            double gx  = pow(abs(gxp - gxn), 2);
            double bx  = pow(abs(bxp - bxn), 2);

			int ryp = pixelColor[x][y - 1].getRed();
			int gyp = pixelColor[x][y - 1].getGreen();
			int byp = pixelColor[x][y - 1].getBlue();
			int ryn = pixelColor[x][y + 1].getRed();
			int gyn = pixelColor[x][y + 1].getGreen();
			int byn = pixelColor[x][y + 1].getBlue();

			double ry  = pow(abs((ryp - ryn)), 2);
            double gy  = pow(abs((gyp - gyn)), 2);
            double by  = pow(abs((byp - byn)), 2);

			double gradX = rx + gx + bx;
			double gradY = ry + gy + by;

			return gradX + gradY;
		}
	}

	public int[] findHorizontalSeam() {
		// sequence of indices for horizontal seam

		transposeColorMatrix();
		CreatePictureFromColorMatrix(height(), width());
		int[] array = findVerticalSeam();
		transposeColorMatrix();
		//CreatePictureFromColorMatrix(width(), height());
		CreatePictureFromColorMatrix(height(), width());
		return array;
	}

	public int[] findVerticalSeam() {
		// sequence of indices for vertical seam

		ST<Double, int[]> shortestPathTable = new ST<Double, int[]>();
		pixelEnergy = new double[width()][height()];

		for (int x = 0; x < width(); x++) {
			for (int y = 0; y < height(); y++) {
				pixelEnergy[x][y] = energy(x, y);
			}
		}

		for (int col = 0; col < width(); col++) {	
			int x = col;
			int y = 0;
			int[] array = new int[height()];
			double pathLength = pixelEnergy[x][y];
			double path1, path2, path3;

			array[y] = x;
			while (y < height() - 1) {
				if (x == 0) {
					path1 = pathLength + pixelEnergy[x][y + 1];	
					path2 = pathLength + pixelEnergy[x + 1][y + 1];	
					if (path1 < path2) {
						pathLength = path1;
					} else {
						pathLength = path2;
						x = x + 1;
					}
				} else if (x == width() - 1) {
					path1 = pathLength + pixelEnergy[x][y + 1];	
					path2 = pathLength + pixelEnergy[x - 1][y + 1];	
					if (path1 < path2) {
						pathLength = path1;
					} else {
						pathLength = path2;
						x = x - 1;
					}
				} else {
					path1 = pathLength + pixelEnergy[x][y + 1];
					path2 = pathLength + pixelEnergy[x - 1][y + 1];
					path3 = pathLength + pixelEnergy[x + 1][y + 1];

					if (path1 <= path2 && path1 <= path3) {
						pathLength = path1;
					} 
					if (path2 <= path1 && path2 <= path3) {
						x = x - 1;
						pathLength = path2;
					} 
					if (path3 <= path1 && path3 <= path2) {
						x = x + 1;
						pathLength = path3;
					}
				}
				y = y + 1;
				array[y] = x;
			}
			shortestPathTable.put(pathLength, array);	
		}

		return shortestPathTable.get(shortestPathTable.min());
		
	}

	public void removeHorizontalSeam(int[] seam) {
		// remove horizontal seam from current picture
		transposeColorMatrix();
		CreatePictureFromColorMatrix(height(), width());
		removeVerticalSeam(seam);
		transposeColorMatrix();
		CreatePictureFromColorMatrix(height(), width());
	}

	public void removeVerticalSeam(int[] seam) {
		// remove vertical seam from current picture
		// Change color Matrix
		for (int row = 0; row < height(); row++) {
			for (int col = seam[row]; col < width() - 1; col++) {
				pixelColor[col][row] = pixelColor[col + 1][row];
			}
		}

		CreatePictureFromColorMatrix(width() - 1, height());
	}
}

