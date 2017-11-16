/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2015-2017 by the contributors of the JetUML project.
 *
 * See: https://github.com/prmr/JetUML
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

/**
 * @author Martin P. Robillard
 */

package ca.mcgill.cs.stg.jetuml.graph.edges;

import ca.mcgill.cs.stg.jetuml.framework.ArrowHead;
import ca.mcgill.cs.stg.jetuml.framework.LineStyle;
import ca.mcgill.cs.stg.jetuml.framework.SegmentationStyleFactory;
import ca.mcgill.cs.stg.jetuml.graph.edges.views.SegmentedEdgeView;

/**
 *  An edge that that represents a generalization of a use case.
 */
public class UseCaseGeneralizationEdge extends AbstractEdge2
{
	/**
	 * Creates a new UseCaseAssociationEdge.
	 */
	public UseCaseGeneralizationEdge()
	{
		aView = new SegmentedEdgeView(this, SegmentationStyleFactory.createStraightStrategy(),
				LineStyle.SOLID, ArrowHead.NONE, () -> ArrowHead.TRIANGLE,
				() -> "", () -> "", () -> "");
	}
}
