package array

func InArr(array []string,column string)(bool){
	for i := 0; i < len(array); i++ {
		if array[i] == column {
			return true
		}
	}
	return false
}
