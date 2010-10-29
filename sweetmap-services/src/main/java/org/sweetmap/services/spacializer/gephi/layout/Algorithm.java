/*
Copyright 2008 WebAtlas
Authors : Mathieu Bastian, Mathieu Jacomy, Julian Bilcke
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.sweetmap.services.spacializer.gephi.layout;

//TODO : commenter la classe Algorithm

/**
 * Define an algorithm implemented in Gephi. Implementing this interface will allow the algorithm be executed
 * correctly by Gephi. Properties of the algorithm can be declared editable and Gephi will recognize them
 * and propose the appropriate form in the {@link PropertiesPanel} in order to modify properties value
 * during the algorithm execution.
 * <p>
 * Properties are made editable when <b>public</b> fields are marked with the {@link EditableProperty} annotation.
 * The system will update directly the fields during the algorithm execution. If you need a <b>setter</b> method,
 * for instance to do some treatments when a property change, just define it and the system will detect and use
 * it to change the property value.
 * <p>
 * The system allows easy algorithm hierarchy. When an algorithm inherit from an another, the properties
 * defined in the father are still available in the child and don't have to be redefined. To complete this
 * mechanism it is recommended to call <code>super.setDefaultValues</code> to also initialize parent properties.
 * <p>
 * <b>Conditions for declaring new algorithm in the system :</b>
 * <br>
 * <ul><li>The class must implement this interface.</li>
 * <li>The class have to inherit from {@link AbstractAlgorithm} or a descendant class.
 * <li>The class should have the {@link AlgorithmConfig} annotation on it.</li>
 * <li>The class should have public fields with the {@link EditableProperty} on it to be editable.</li></ul>
 * <p>
 * Algorithm can have access to the current {@link Space} and the current {@link AlgosController} (this
 * is useful to make algorithms stop by themselves).
 *
 * @author Mathieu Bastian
 */

public interface Algorithm {


  /**
   * Cette méthode initialise les valeurs des champs internes que vous avez décider d'ajouter
   * comme propriété. Concrètement c'est cette fonction qui est appelé lors du
   * rechargement des propriétés. Elle est aussi appelée à l'instanciation.
   */
  void setDefaultValues();




  /**
   * Appelé lors de l'execution de l'algorithme à chaque itération.
   */
  void goAlgo();


  /**
   * Appelé une seule fois avant le demarrage de l'algorithme.
   * @return .
   */
  boolean initAlgo();

}
