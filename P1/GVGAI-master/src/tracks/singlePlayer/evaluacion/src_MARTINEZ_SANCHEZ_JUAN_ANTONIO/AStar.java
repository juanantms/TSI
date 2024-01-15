package tracks.singlePlayer.evaluacion.src_MARTINEZ_SANCHEZ_JUAN_ANTONIO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.Vector2d;

public class AStar {

	ArrayList<Nodo> ruta;
	
	private Comparator<Nodo> comp;
	
	StateObservation state;
	public ArrayList<Observation> grid[][];
	ArrayList<Integer> obstacleItypes;
	
	public int nodosexpandidos;
	public int maximoNodos;
	
	private PriorityQueue<Nodo> abiertos;
    private Map<String, Double> cerrados;
	
	private static int[] x_arrNeig = null;
    private static int[] y_arrNeig = null;
	
	public AStar(StateObservation stateObs) {
		
		this.grid = stateObs.getObservationGrid();
		state = stateObs;
		
		obstacleItypes = new ArrayList<>();       
        obstacleItypes.add(0); // muros
        obstacleItypes.add(4); // trampa
		
		ruta = new ArrayList<>();
		nodosexpandidos = 0;
		maximoNodos = 0;
		
		init();
		
		abiertos = new PriorityQueue<>(comp);
        cerrados = new HashMap<>();
		
		comp = new Comparator<Nodo>() {
			public int compare(Nodo n1, Nodo n2) {
		    	return n1.compareTo(n2);
		    }
		};
		
	}
	
	
	/**
	 * Función extraída de la clase PathFinder.
	 * Inicializa dos arrays para luego generar nodos.
	 */
	private void init()
    {
        if(x_arrNeig == null)
        {
            //TODO: This is a bit of a hack, it wouldn't work with other (new) action sets.
            ArrayList<Types.ACTIONS> actions = this.state.getAvailableActions();
            if(actions.size() == 3)
            {
                //left, right
                x_arrNeig = new int[]{-1, 1};
                y_arrNeig = new int[]{0,  0};
            }else
            {
                //up, down, left, right
                x_arrNeig = new int[]{0,    0,    -1,    1};
                y_arrNeig = new int[]{-1,   1,     0,    0};
            }
        }
    }
	
	
	/**
	 * Función extraída de la clase PathFinder.
	 * Comprueba en el tablero si hay obstáculo.
	 */
	public boolean isObstacle(int row, int col)
    {
        if(row<0 || row>=grid.length) return true;
        if(col<0 || col>=grid[row].length) return true;

        for(Observation obs : grid[row][col])
        {
            if(obstacleItypes.contains(obs.itype))
                return true;
        }

        return false;

    }
	
	
	/**
	 * 
	 * Función extraída de la clase PathFinder y adaptada a la práctica.
	 * Genera los hijos del nodo pasado por parámetro con el orden de hijo Arriba, hijo Abajo, hijo Izquierdo y hijo Derecho.
	 * Devuelve los hijos generados en un ArrayList de nodos inicializados con su posición correspondiente y con 'nodo' como padre.
	 * Asigna el movimiento que se realiza para llegar desde el padre.
	 *
	 * @param nodo del que se generan los hijos.
	 * @return ArrayList<Nodo> 
	 */
	public ArrayList<Nodo> getNeighbours(Nodo nodo) {
        ArrayList<Nodo> neighbours = new ArrayList<Nodo>();
        int x = (int) (nodo.position.x);
        int y = (int) (nodo.position.y);

        for(int i = 0; i < x_arrNeig.length; ++i)
        {
            if(!isObstacle(x+x_arrNeig[i], y+y_arrNeig[i]))
            {
            	Nodo n = new Nodo(new Vector2d(x+x_arrNeig[i], y+y_arrNeig[i]), nodo);
            	
            	if(nodo.position.x < n.position.x) {
	                n.accion = Types.ACTIONS.ACTION_RIGHT;   
	            }else if(nodo.position.x > n.position.x) {
	            	n.accion = Types.ACTIONS.ACTION_LEFT;
	            }else if(nodo.position.y < n.position.y) {
	            	n.accion = Types.ACTIONS.ACTION_DOWN;
	            }else if(nodo.position.y > n.position.y) {
	            	n.accion =  Types.ACTIONS.ACTION_UP;
	            }

                neighbours.add(n);
            }
        }

        return neighbours;
    }
	
	
	/**
	 * 
	 * Función extraída de la clase PathFinder.
	 * Calcula el camino desde el Nodo nodo hasta el nodo inicial.
	 * 
	 * @param nodo
	 * @return ArrayList<Nodo>
	 */
	private LinkedList<Nodo> calcularPath(Nodo nodo) {
		LinkedList<Nodo> path = new LinkedList<>();
		while(nodo != null) {
			if(nodo.parent != null) {
				nodo.setMoveDir(nodo.parent);
				path.add(0, nodo);
			}
			nodo = (Nodo) nodo.parent;
		}
		return path;
	}
	
	
	/**
	 * 
	 * Función que calcula la distancia Manhattan desde current hasta objetivo
	 * 
	 * @param current
	 * @param objetivo
	 * @return distancia Manhattan
	 */
	public double DistanciaManhattan(Nodo current, Nodo objetivo) {
		double Diffx = Math.abs(current.position.x - objetivo.position.x);
        double Diffy = Math.abs(current.position.y - objetivo.position.y);

        return Diffx+Diffy;
	}
	
	
	/**
	 * 
	 * ALGORITMO DE BUSQUEDA A*:
	 * 
	 * Utiliza un ArrayList de abiertos para los nodos no visitados y otro ArrayList de cerrados para los nodos visitados.
	 * 
	 * @param inicial
	 * @param objetivo
	 * @return
	 */
	public LinkedList<Nodo> Astar(Nodo inicial, Nodo objetivo) {
		     
        // Actualizamos los costes del nodo inicial.
        inicial.totalCost = 0.0f;
        inicial.parent = null;
        inicial.estimatedCost = DistanciaManhattan(inicial, objetivo);
        
        // Añadimos a abiertos
        abiertos.add(inicial);
        Nodo current, nodo;
        double distancia;
        ArrayList<Nodo> hijos = new ArrayList<>();
        
        while(true) {
        	
        	// Cogemos el primer nodo de abiertos que será el de menos coste de los nodos generados.
        	current = abiertos.poll();
        	// Lo añadimos a cerrados
        	cerrados.put(current.position.toString(), current.totalCost);
        	
        	// Expandimos el nodo actual y comprobamos si es objetivo.
        	nodosexpandidos++;
        	if(current.position.equals(objetivo.position)) {
        		// Si es objetivo devolvemos la ruta con la función calcularPath().
        		return calcularPath(current);
        	}
        	
        	// Si no es objetivo generaremos los nodos hijos del nodo 'current' con la función getNeighbours().
        	hijos = getNeighbours(current);
        	
        	// Recorremos los hijos generados.
        	for(int i=0; i<hijos.size(); i++) {
        		
        		nodo = hijos.get(i);
        		distancia = nodo.totalCost;
        		
        		if(nodo!=current.parent) {
        			// Si no está  en cerrados ni en abiertos:
        			if(!cerrados.containsKey(nodo.position.toString()) && !abiertos.contains(nodo)) {
	    				// Actualizamos los costes.
	    				nodo.totalCost = distancia + current.totalCost;
	    				nodo.estimatedCost += DistanciaManhattan(nodo, objetivo);
	    				// Lo añadimos a abiertos para volver a expandirlo.
	    				abiertos.add(nodo);
	    				
        			// Si el hijo está en cerrados y es más prometedor:
        			}else if(cerrados.containsKey(nodo.position.toString()) && distancia + current.totalCost < cerrados.get(nodo.position.toString())) {
        				cerrados.remove(nodo.position.toString());
        				// Actualizamos los costes.
        				nodo.totalCost = distancia + current.totalCost;
        				nodo.estimatedCost += DistanciaManhattan(nodo, objetivo);
        				// Lo añadimos a abiertos para volver a expandirlo.
        				abiertos.add(nodo);
	        		}
        		}
        	}
        	
        	// Almacenamos el maximo de nodos en memoria.
        	if(cerrados.size()+abiertos.size() > maximoNodos) {
        		maximoNodos = cerrados.size()+abiertos.size(); 
        	}
        }
	}
	

}
