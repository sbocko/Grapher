package sk.upjs.kombinatorika;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

import sk.upjs.paz.Graph;

public class GraphReader {
	
	public Graph readGraphFromIncidencyMatrix(double[][] matrix){
		Graph graph = new Graph();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if(matrix[i][j] != 0){
					graph.addEdge(graph.getOrCreateVertex(""+(i+1)), graph.getOrCreateVertex(""+(j+1)));
				}
			}
		}
		
		return graph;
	}
	
	/**
	 * 
	 * @param filename the path to file with incidency matrix
	 * @return
	 */
	public double[][] readMatrixFromFile(String filename){
		double[][] matrix = null;
		
		File file = new File(filename);
		Scanner scanner = null;
		try{
			scanner = new Scanner(file);
			int matrixLength = readMatrixSize(filename);
			matrix = new double[matrixLength][matrixLength];
			
			Pattern pattern = Pattern.compile(",");
			for (int i = 0; i < matrixLength; i++) {
				String line = scanner.nextLine();	
				String[] incidencyValues = pattern.split(line);
				if(incidencyValues.length != matrixLength){
					throw new Exception("Error reading matrix on line " + i+1);
				}
				for (int j = 0; j < matrixLength; j++) {
					matrix[i][j] = Double.parseDouble(incidencyValues[j]);
				}	
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(scanner != null){
				scanner.close();
			}
		}
		
		return matrix;
	}
	
	/**
	 * Returns the length of incidency matrix from file based on the first line of this matrix.
	 * A ',' is used as a delimiter character.
	 * @param filename the path to file with incidency matrix
	 * @return the length of matrix
	 */
	private int readMatrixSize(String filename){
		File file = new File(filename);
		Scanner scanner = null;
		try{
			scanner = new Scanner(file);
			String line = scanner.nextLine();
			Pattern pattern = Pattern.compile(","); 
			String[] incidencyValues = pattern.split(line);
			return incidencyValues.length;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(scanner != null){
				scanner.close();
			}
		}
		return 0;
	}
}
