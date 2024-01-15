package tracks.singlePlayer.evaluacion.src_MARTINEZ_SANCHEZ_JUAN_ANTONIO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.Vector2d;

public class IDAStar {
	
	LinkedList<Nodo> ruta;
	double cota;
	
	StateObservation state;
	public ArrayList<Observation> grid[][];
	ArrayList<Integer> obstacleItypes;
	
	public int nodosexpandidos;
	public int maximoNodos;
	
	private static int[] x_arrNeig = null;
    private static int[] y_arrNeig = null;
    
    private Comparator<Nodo> comp;
	
	
	public IDAStar(StateObservation stateObs) {
		this.grid = stateObs.getObservationGrid();
		state = stateObs;
		
		obstacleItypes = new ArrayList<>();       
        obstacleItypes.add(0); // muros
        obstacleItypes.add(4); // trampa
		
		ruta = new LinkedList<>();
		nodosexpandidos = 0;
		maximoNodos = 0; 
		
		init();
		
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
	
	
	
	public LinkedList<Nodo> IDAstar(Nodo inicial, Nodo objetivo) {
		
		// Asignamos el coste estimado a inicial con la distancia Manhattan.
		inicial.estimatedCost = DistanciaManhattan(inicial, objetivo);
		// Asignamos la cota.
		cota = inicial.estimatedCost;
		// Añadimos a la ruta el nodo inicial.
		ruta.addLast(inicial);
		
		while(true) {
			// Llamamos a search para buscar el nodo objetivo con la cota actual.
			double t = search(ruta, 0, cota, objetivo);
			// Si t vale 1.0f quiere decir que encontramos el objetivo y devolvemos la ruta.
			if(t==1.0f) {
				maximoNodos = ruta.size();
				return ruta;
			// Si no se ha encontrado establecemos la nueva cota y seguimos llamando a search.
			}else {
				cota = t;
			}
		}
	}
	
	
	public double search(LinkedList<Nodo> ruta, double g, double cota, Nodo objetivo) {
		
		// Obtenemos el último nodo de la ruta
		Nodo current = ruta.getLast();
		
		// Obtenemos f
		double f = g + current.estimatedCost;
		
		// Si f supera la cota paramos y devolvemos f.
		if(f>cota) {
			return f;
		}
		
		nodosexpandidos++;
		// Si el nodo es objetivo devolveremos 1.0f ya que habremos encontrado la ruta.
		if(current.position.equals(objetivo.position)) {
    		return 1.0f;
    	}
		
		double min = 999999999.9f;
		
		// Calculamos los hijos del nodo actual y calculamos sus costes.
		ArrayList<Nodo> hijos = getNeighbours(current);
    	for(int i=0; i<hijos.size(); i++) {
    		hijos.get(i).totalCost += current.totalCost;
    		hijos.get(i).estimatedCost = DistanciaManhattan(hijos.get(i), objetivo);
    	}
    	// Los ordenamos por c(nodo, v) + h(v)
    	Collections.sort(hijos, comp);
    	
    	
    	for(int i=0; i<hijos.size(); i++) {
    		
    		// SI el hijo no está en la ruta:
    		if(!ruta.contains(hijos.get(i))) {
    			// lo añadimos al final de la ruta
    			ruta.addLast(hijos.get(i));
    			//double t = search(ruta, g+hijos.get(i).totalCost, cota, objetivo);
    			// comprobamos si la ruta llega al objetivo
    			double t = search(ruta, g+1, cota, objetivo);
    			// Si t vale 1.0f hanremos encontrado la ruta
    			if(t==1.0f) {
    				return 1.0f;
    			}
    			// si t es menor que el minimo actual, actualizamos el minimo
    			if(t<min) {
    				min = t;
    			}
    			// llegados a este punto borraremos el ultimo hijo añadido.
    			ruta.pollLast();
    		}
    	}
    	// si no se ha encontrado el objetivo devolvemos el minimo.
		return min;
	}
	
	
}
