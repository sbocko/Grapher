package sk.upjs.kombinatorika;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Pattern;
import sk.upjs.paz.Edge;
import sk.upjs.paz.Graph;

public class GraphReader {
    
        /**
         * Reads graph from file. 
         * @param filename path to the file with graph
         * @return a graph instance. Null if not successfull.
         */
        public Graph readGraphFromFile(String filename){            
            Graph graph = readGraphFromIncidencyMatrix(filename);
            
            if(graph == null){
                boolean successfull = graph.loadFromFile(filename);
                if(!successfull){
                    graph = null;
                }
            }
            
            return graph;
        }
	
        /**
         * Creates graph from its incidency matrix stored in file.
         * @param filename the path to file with incidency matrix
	 * @return graph that corresponds to the incidency matrix
         */
	private Graph readGraphFromIncidencyMatrix(String filename){
                double[][] matrix = readMatrixFromFile(filename);
                if(matrix == null){
                    return null;
                }
                
		Graph graph = new Graph();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if(matrix[i][j] != 0){
                                    Edge edge =graph.addEdge(graph.getOrCreateVertex(""+(i+1)), graph.getOrCreateVertex(""+(j+1)));
                                    edge.setWeight(matrix[i][j]);
				}
			}
		}
		
		return graph;
	}
	
	/**
	 * Reads the incidency matrix from file. Returns null, if some problem occures.
	 * @param filename the path to file with incidency matrix
	 * @return graph incidency matrix
	 */
	private double[][] readMatrixFromFile(String filename){
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
                        return null;
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
                return -1;
	}
}
