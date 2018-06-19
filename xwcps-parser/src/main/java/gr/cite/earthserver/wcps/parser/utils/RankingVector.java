package gr.cite.earthserver.wcps.parser.utils;

import java.util.ArrayList;
import java.util.List;

import gr.cite.earthserver.wcs.core.Coverage;

public class RankingVector implements Comparable<RankingVector>
{
	private List<RankDefinition> rankDefinitions;
	
	private Object[] values;
	
	private Coverage item;
	
	public RankingVector(Coverage item, List<RankDefinition> rankDefinitions)
	{
		this.values = new Object[rankDefinitions.size()];
		this.item = item;
		this.rankDefinitions = new ArrayList<RankDefinition>();
		this.rankDefinitions.addAll(rankDefinitions);
	}

	public int resolveDirection(int ordinal, int res) throws Exception
	{
		switch (this.rankDefinitions.get(ordinal).getDirection())
		{
			case Ascending: return res;
			case Descending: return (-1 * res);
			default: throw new Exception("unrecognized type");
		}
	}

	@Override
	public int compareTo(RankingVector o) {
		if (o == null) return 1;
		if (!(o instanceof RankingVector)) return 1;
		
		for (int i = 0; i < this.values.length; i += 1)
		{
			if (this.values[i] != null && o.values[i] == null) return  -1;
			else if (this.values[i] == null && o.values[i] != null) return 1;
			else if (this.values[i] == null && o.values[i] == null) continue;
			else
			{
				int res = ((Comparable)this.values[i]).compareTo(o.values[i]);
				if (res != 0)
					try {
						return this.resolveDirection(i, res);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
		
		return 0;
	}

	public Coverage getItem() {
		return this.item;
	}
	
	public Object[] getValues() {
		return this.values;
	}
}
