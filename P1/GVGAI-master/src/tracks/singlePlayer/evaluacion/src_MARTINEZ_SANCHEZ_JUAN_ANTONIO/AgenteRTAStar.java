package tracks.singlePlayer.evaluacion.src_MARTINEZ_SANCHEZ_JUAN_ANTONIO;

import java.util.ArrayList;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

public class AgenteRTAStar extends AbstractPlayer{
	
	private Vector2d fescala;
	private Nodo inicial;
	private Nodo objetivo;
	private Vector2d portal;
	private boolean empieza;
	
	private ArrayList<Nodo> ruta;
	
	 
	public AgenteRTAStar(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		
		fescala = new Vector2d(stateObs.getWorldDimension().width / stateObs.getObservationGrid().length,
				stateObs.getWorldDimension().getHeight() / stateObs.getObservationGrid()[0].length);
		
		ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());
		
		portal = posiciones[0].get(0).position;
		portal.x = Math.floor(portal.x / fescala.x);
		portal.y = Math.floor(portal.y / fescala.y);
		empieza = false;
		Vector2d pos_ini = new Vector2d(stateObs.getAvatarPosition().x / 
        		fescala.x, stateObs.getAvatarPosition().y / fescala.y);
        
        inicial = new Nodo(pos_ini);
        objetivo = new Nodo(portal);
        
	}
	
	@Override
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
      
		if(!empieza) {
			empieza = true;
			AStar astar = new AStar(stateObs);

	        long tInicio = System.nanoTime();
	        ruta = astar.Astar(inicial, objetivo);
	        long tFin = System.nanoTime();
	        long tiempoTotalenMiliSegundos = (tFin - tInicio)/1000000;
	        
	        System.out.println("Tamaño de la ruta: " + ruta.size());
	        System.out.println("Nodos expandidos: " + astar.nodosexpandidos);
	        System.out.println("Nodos en memoria: " + astar.maximoNodos);
	        System.out.println("Tiempo acumulado: " + tiempoTotalenMiliSegundos);
		
		}
		
	
        
		Types.ACTIONS a = ruta.get(0).accion;
		ruta.remove(0);
		
		return a;
	}

}
