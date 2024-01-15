package tracks.singlePlayer.evaluacion.src_MARTINEZ_SANCHEZ_JUAN_ANTONIO;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.Vector2d;

public class BFS {

	public Queue<Nodo> Q;
	/*
	 * Almacena nodos visitados
	 */
	public Hashtable<String, Boolean> visitados;
	
	StateObservation state;
	public ArrayList<Observation> grid[][];
	ArrayList<Integer> obstacleItypes;
	
	public int nodosexpandidos;
	public int maximoNodos;
	
	private static int[] x_arrNeig = null;
    private static int[] y_arrNeig = null;
	
	
	public BFS(StateObservation stateObs) {
		this.grid = stateObs.getObservationGrid();
		state = stateObs;
		
		obstacleItypes = new ArrayList<>();       
        obstacleItypes.add(0); // muros
        obstacleItypes.add(4); // trampa
        
		Q = new LinkedList<>();
		
		nodosexpandidos = 0;
		maximoNodos = 0;
		
		init();
		
		visitados = new Hashtable<>();
	}
	

	/**
	 * Función extraída de la clase PathFinder.
	 * Inicializa dos arrays para luego generar nodos.
	 */
	private void init()
    {
        if(x_arrNeig == null)
        {
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
        	// Si no es obstáculo generamos hijo.
            if(!isObstacle(x+x_arrNeig[i], y+y_arrNeig[i]))
            {
            	Nodo n = new Nodo(new Vector2d(x+x_arrNeig[i], y+y_arrNeig[i]), nodo);
            	
            	// Asignamos la accion en función de la variación en las coordenadas respecto al padre.
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
	 * ALGORITMO DE BUSQUEDA BFS:
	 * 
	 * Utilizaremos una única cola Q y marcaremos los nodos como visitado (true) o no visitado (false).
	 * 
	 * @param inicial: Nodo inicial desde se empiza la búsqueda.
	 * @param objetivo: Nodo objetivo.
	 * @return ArrayList<Nodo> con la ruta hasta el nodo objetivo.
	 */
	public LinkedList<Nodo> Anchura(Nodo inicial, Nodo objetivo) {
	
		// Marcamos el inicial como visitado en la tabla Hash visitados.
		visitados.put(inicial.position.toString(), true);
		inicial.parent = null;
		// Añadimos a la cola.
        Q.add(inicial);       
        
        Nodo current, nodo;
        ArrayList<Nodo> hijos;
        
        // Mientras la cola no esté vacía:
        while(!Q.isEmpty()) {
        	
        	// Cogemos el nodo del frente de la cola
        	current = Q.poll();

        	
        	// Expandimos el nodo actual y comprobamos si es objetivo.
        	nodosexpandidos++;
        	if(current.position.equals(objetivo.position)) {
        		maximoNodos = visitados.size();
        		// Si es objetivo devolvemos la ruta con la función calcularPath().
        		return calcularPath(current);
        	}
        	
        	// Si no es objetivo generaremos los nodos hijos del nodo actual 'current' con la función getNeighbours().
        	hijos = getNeighbours(current);
        	for(int i=0; i<hijos.size(); i++) {
        		nodo = hijos.get(i);
        		// Si el hijo generado no está visitado lo añadimos.
        		if(!visitados.containsKey(nodo.position.toString())) {
        			// Lo marcamos como visitado introduciendolo a la tablas Hash.
        			visitados.put(nodo.position.toString(), true);
        			// Lo añadimos a la cola.
        			Q.add(nodo);
        		}
        	}
        }
      
        // En el caso de que no haya encontrado camino devolvemos la ruta con el nodo inicial.
        return calcularPath(inicial);
	}
	
	
}
