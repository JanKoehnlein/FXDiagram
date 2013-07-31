package de.fxdiagram.examples.lcars

import java.util.regex.Pattern

class NameShortener {
	
	val initialsPattern = Pattern.compile('((^|\\s)[A-Z]\\.\\s*)')
	val parenthesesPattern = Pattern.compile('\\s*\\([^\\)]+\\)')
	
	def shortenName(String name) {
		var matcher = initialsPattern.matcher(name)
		if(matcher.find)
			return matcher.replaceAll(' ')	
		matcher = parenthesesPattern.matcher(name)
		if(matcher.find)
			return matcher.replaceAll('()')
		val subnames = name.split(' ')
		switch subnames.size {
			case 1: {
				return subnames.head.substring(0, 8) + '...'
			}
			case 2: {
				val firstName = subnames.head
				if(firstName.length <= 2)
					return subnames.last
				else 
					return firstName.charAt(0) + '. ' + subnames.last  			
			}
			default:
				return subnames.head + ' ' + subnames.last
		}
	}
}