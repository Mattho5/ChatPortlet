package sk.mattho.portlets.chatPortlet;

import java.util.ArrayList;
import java.util.List;

public class PreferencesAccounts {
	private static String ACCOUNT_SEPARATOR= "[[";
	private static String SEPARATOR_REGEX="\\[\\[";
	private List<AccountInfo> accounts;
	
	
	public PreferencesAccounts(){
		this.accounts= new ArrayList<AccountInfo>();
	}
	
	public PreferencesAccounts(String accs){
		this.accounts= new ArrayList<AccountInfo>();
		
		for(String accString: accs.split("\\[\\[")){
			this.accounts.add(new AccountInfo(accString));
		}
	}
	
	public List<AccountInfo> accounts(){
		return accounts;
	}
	public String getAccounts() {
		String temp="";
		
		for(AccountInfo a:accounts){
			temp+=a.toString()+ ACCOUNT_SEPARATOR;
		
		}
		
		return temp;
		
	}
	
	public void addAccount(AccountInfo a){
		this.accounts.add(a);
		}

}
