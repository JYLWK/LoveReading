package hit.cs.iread;

/*
 * 计算某个月第一天是周几
 */

public class CalculateFirstday {
	
	static final int YEAR = 2013 ;
	static final int MONTH = 1 ;
	static final int DAY = 1 ;
	static final int WHICHDAY=2 ;
	
	private int year ;
	private int month ;
	private int daysofmonth[] = {31,0,31,30,31,30,31,31,30,31,30,31};
	public CalculateFirstday(int _year , int _month ){
		this.year = _year;
		this.month = _month ;
	}
	
	public int getFirstday(){
		int day=0 ;
		day += dayofyear();
		day += dayofmonth() ;
		day = (day) % 7 ;
		day = WHICHDAY+day ;
		if (day>7){
			day = (day+1)%7;
		}
		return day ;
	}
	
	public int dayofyear(){
		int day=0;
		int y=this.year ;
		while ( y!=YEAR ){
			if ( isLeapyear() )  {
				day+=366 ;
			}
			else day+=365 ;
			y--;
		}
		return day ;
	}
	
	public int dayofmonth(){
		int day = 0 ;
		int m = this.month ;
		m--;
		while ( m+1>MONTH ){
			
			if ( m==2 ){
				if (isLeapyear()){
					day+=29;
				}
				else day+=28;
			}
			else{
				day += daysofmonth[m-1] ;
			}
			
			m--;
		}
		return day ;
	}
	
	public boolean isLeapyear(){
		if ( ( this.year%4==0 && this.year%100!=0 ) || ( this.year%400==0) ){
			return true ;
		}
		return false ;
	}
	
	public int getEndday(){
		if ( this.month==2 ){
			if ( isLeapyear() ){
				return 29 ;
			}
			else return 28 ;
		}
		else {
			return daysofmonth[this.month-1];
		}
	}
	
	public int getYear(){
		return year ;
	}
	
	public int getMonth(){
		return month;
	}
}
