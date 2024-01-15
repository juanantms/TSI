package tracks.singlePlayer.evaluacion.src_MARTINEZ_SANCHEZ_JUAN_ANTONIO;

import ontology.Types;
import tools.Vector2d;
import tools.pathfinder.Node;

public class Nodo extends Node{
	
	public Types.ACTIONS accion;


	public Nodo(Vector2d pos) {
		super(pos);
		accion = Types.ACTIONS.ACTION_NIL;
	}
	
	public Nodo(Vector2d pos, Node padre) {
		super(pos);
		this.parent = padre;
	}
	



}
