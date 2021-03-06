angular.module('app').factory('autocompletesDataFactory', function () {
  return {
    Currency: {
      valueProperty: 'sName_UA',
      titleProperty: 'sName_UA',
      prefixAssociatedField: 'sID_UA',
      apiUrl: './api/currencies',
      orderBy: 'sName_UA'
    },
    ObjectCustoms: {
      valueProperty: 'sName_UA',
      titleProperty: 'sName_UA',
      prefixAssociatedField: 'sID_UA',
      orderBy: 'sName_UA',
      apiUrl: './api/object-customs',
      hasPaging: true
    },
    SubjectOrganJoinTax: {
      valueProperty: 'sName_UA',
      titleProperty: 'sName_UA',
      orderBy: 'sID_UA',
      prefixAssociatedField: 'sID_UA',
      apiUrl: './api/subject/organs/join-tax',
      init: function (scope) {
        scope.$watch("formData.params['sID_Public_SubjectOrganJoin'].nID", function (newValue) {
          scope.refreshList('nID_SubjectOrganJoin', newValue);
        });
      }
    },
    ObjectEarthTarget: {
      valueProperty: 'sName_UA',
      titleProperty: 'sName_UA',
      orderBy: 'sName_UA',
      prefixAssociatedField: 'sID_UA',
      apiUrl: './api/object-earth-target'
    },
    Country: {
      valueProperty: 'sNameShort_UA',
      titleProperty: 'sNameShort_UA',
      orderBy: 'sNameShort_UA',
      prefixAssociatedField: 'nID_UA',
      additionalValueProperty: 'sID_UA',
      apiUrl: './api/countries/'
    },
    ID_SubjectActionKVED: {
      valueProperty: 'sID',
      titleProperty: 'sFind',
      orderBy: 'nID',
      apiUrl: './api/subject-action-kved'
    },
    ID_ObjectPlace_UA: {
      valueProperty: 'sID',
      titleProperty: 'sFind',
      orderBy: 'nID',
      apiUrl: './api/object-place'
    }
  }
});
